package tingeso.prestabanco.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tingeso.prestabanco.dto.*;
import tingeso.prestabanco.model.*;
import tingeso.prestabanco.repository.*;

import java.io.IOException;
import java.util.*;
import java.sql.Date;

@Service
public class MortgageLoanService {
    @Autowired
    MortgageLoanRepository mortgageLoanRepository;

    @Autowired
    MortgageLoanReviewRepository mortgageLoanReviewRepository;

    @Autowired
    MortgageLoanPendingDocumentationRepository pendingDocumentationRepository;

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    LoanStatusRepository loanStatusRepository;

    @Autowired
    LoanTypeRepository loanTypeRepository;



    @Value("${bank.quota_income_threshold}")
    float quotaIncomeThreshold;


    public MortgageLoanModel getMortgageLoan(Long mortgage_loan_id, Authentication auth) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
            ClientModel client = (ClientModel) auth.getPrincipal();
            if (!mortgage.getClient().getId().equals(client.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        if (mortgage.getStatus().getId().equals("E2")) {
            System.out.println("le faltan docs");
            return pendingDocumentationRepository.getReferenceById(mortgage_loan_id);
        }
        return mortgage;

    }


    public MortgageLoanModel receiveMortgage(MortgageLoanRequest req, ClientModel client) {
        System.out.println("AGREGANDO COSA");
        Optional<LoanTypeModel> loan_type = loanTypeRepository.findById(req.getLoan_type_id());
        if (loan_type.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan type does not exist");
        }

        LoanStatusModel loan_status = getLoanStatus("E1");

        MortgageLoanModel mortgage_loan = new MortgageLoanModel(req,
                                                               client,
                                                               loan_status,
                                                               loan_type.get());
        System.out.println(mortgage_loan.toString());
        System.out.println(mortgage_loan.getMonthlyQuota());
        mortgageLoanRepository.save(mortgage_loan);

        return mortgage_loan;
    }

    public SimpleResponse addDocuments(Long mortgage_loan_id, MultipartFile[] docs, ClientModel client) {
        MortgageLoanModel mortgage_loan = getMortgageLoan(mortgage_loan_id);
        validateMortgageOwnership(mortgage_loan, client);
        if (docs.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must send at least one new document");
        }
        Boolean is_on_E1 = mortgage_loan.getStatus().getId().equals("E1");
        Boolean is_on_E2 = mortgage_loan.getStatus().getId().equals("E2");

        if (!(is_on_E1 || is_on_E2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not add documents to a request in evaluation of further steps");
        }

        // Update the mortgage_loan status so it can be reviewed again
        LoanStatusModel loan_status = getLoanStatus("E1");
        mortgage_loan.addDocuments(docs);
        mortgage_loan.setStatus(loan_status);
        System.out.println(mortgage_loan.getDocuments().get(0).getName());
        mortgageLoanRepository.save(mortgage_loan);
        return new SimpleResponse("Documents added successfully");
    }

    public SimpleResponse setPendingDocumentation(Long mortgage_loan_id, PendingDocumentationRequest req, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        if (!mortgage.getStatus().getId().equals("E1")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be set in evaluation yet");
        }
        LoanStatusModel loan_status = getLoanStatus("E2");
        mortgage.setStatus(loan_status);
        MortgageLoanPendingDocumentationModel pending = new MortgageLoanPendingDocumentationModel(mortgage);
        List<DocumentTypeModel> docs = documentTypeRepository.findAllByIdIn(req.getDocument_ids());
        pending.addMissingDocuments(docs);
        pending.setDetails(req.getDetails());
        pendingDocumentationRepository.save(pending);
        mortgageLoanRepository.save(mortgage);

        return new SimpleResponse("Mortgage loan updated");
    }

    public SimpleResponse setInEvaluation(Long mortgage_loan_id, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        if (!mortgage.getStatus().getId().equals("E1")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be set in evaluation, due to " +
                    "not having enough documents or because it is already on a more advanced part of the process");
        }
        LoanStatusModel new_loan_status = getLoanStatus("E3");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage loan updated");
    }

    public SimpleResponse evaluateMortgage(Long mortgage_loan_id, CreditEvaluation evaluation, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        if (!mortgage.getStatus().getId().equals("E3")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be evaluated, as its missing " +
                    "documents or it has already been evaluated");
        }

        try {
            validateCredit(mortgage, evaluation.getCredit_validation());
        } catch (Exception e) {
            return setRejected(mortgage_loan_id, executive);
        }

        int saving_score = sumSavingsScore(mortgage, evaluation.getSaving_capacity());
        if (saving_score > 4) {
            return setApproved(mortgage_loan_id, executive);
        }
        if (saving_score > 2) {
            return setRequiresReview(mortgage_loan_id, executive);
        }

        return setRejected(mortgage_loan_id, executive);
    }

    public SimpleResponse setPreApproved(Long mortgage_loan_id, ExecutiveModel executive) {
        validateMortgageReview(mortgage_loan_id);
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        LoanStatusModel new_loan_status = getLoanStatus("E4");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage loan updated");

    }

    public SimpleResponse setFinalApproval(Long mortgage_loan_id, ClientModel client) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        validateMortgageOwnership(mortgage, client);
        LoanStatusModel request_status = mortgage.getStatus();
        if (!request_status.getId().equals("E4")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be set to final " +
                    "approval yet, as it is has not been pre-approved yet");
        }

        LoanStatusModel new_loan_status = getLoanStatus("E5");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage loan updated");
    }

    public SimpleResponse setApproved(Long mortgage_loan_id, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        LoanStatusModel request_status = mortgage.getStatus();
        if (!request_status.getId().equals("E5")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be approved, " +
                    "as it has not been final approved by the client yet");
        }

        LoanStatusModel new_loan_status = getLoanStatus("E6");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage loan updated");
    }

    public SimpleResponse setRejected(Long mortgage_loan_id, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        LoanStatusModel request_status = mortgage.getStatus();
        if (!request_status.getId().equals("E3")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be rejected, as it is not on evaluation period");
        }
        LoanStatusModel new_loan_status = getLoanStatus("E7");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage has been rejected");
    }

    public SimpleResponse cancelMortgageByClient(Long mortgage_loan_id, ClientModel client) {
        MortgageLoanModel mortgage_loan = getMortgageLoan(mortgage_loan_id);
        validateMortgageOwnership(mortgage_loan, client);

        LoanStatusModel loan_status = getLoanStatus("E8");

        mortgage_loan.setStatus(loan_status);
        mortgageLoanRepository.save(mortgage_loan);
        return new SimpleResponse("Mortgage loan cancelled");

    }

    public SimpleResponse setInOutgo(Long mortgage_loan_id, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        LoanStatusModel request_status = mortgage.getStatus();
        if (!request_status.getId().equals("E6")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mortgage loan can not be set in outgo, as it has not been approved");

        }

        LoanStatusModel new_loan_status = getLoanStatus("E9");
        mortgage.setStatus(new_loan_status);
        mortgageLoanRepository.save(mortgage);
        return new SimpleResponse("Mortgage loan updated");
    }

    private MortgageLoanModel getMortgageLoan(Long id) {
        Optional<MortgageLoanModel> mortgage = mortgageLoanRepository.findById(id);
        if (mortgage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The mortgage loan requested does not exist");
        }
        return mortgage.get();
    }

    private void validateMortgageOwnership(MortgageLoanModel mortgage_loan, ClientModel client) {
        ClientModel mortgage_owner = mortgage_loan.getClient();
        if (!mortgage_owner.getId().equals(client.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this mortgage loan");
        }
    }

    private LoanStatusModel getLoanStatus(String id) {
        Optional<LoanStatusModel> loan_status = loanStatusRepository.findById(id);
        if (loan_status.isEmpty()) {
            throw new IllegalArgumentException("The loan status requested does not exist");
        }
        return loan_status.get();
    }

    private void validateQuotaIncomeRelation(MortgageLoanModel mortgage, Long income) {
        Long monthly_quota = mortgage.getMonthlyQuota();
        float relation = (float) monthly_quota / income;
        if (relation < quotaIncomeThreshold) {
            throw new IllegalArgumentException("Quota exceeded");
        }
    }

    private void validateCreditHistory(Boolean has_acceptable_history) {
        if (!has_acceptable_history) {
            throw new IllegalArgumentException("Credit history not valid");
        }
    }

    private void validateFinancialStability(Boolean has_financial_stability) {
        if (!has_financial_stability) {
            throw new IllegalArgumentException("Financial stability not valid");
        }
    }

    private void validateDebtIncomeRelation(MortgageLoanModel mortgage, Long income, Long monthly_debt) {
        Long monthly_quota = mortgage.getMonthlyQuota();
        float relation = (float) (monthly_quota + monthly_debt) / income;
        if (relation < quotaIncomeThreshold) {
            throw new IllegalArgumentException("Debt exceeded");
        }
    }

    private void validateMaxFinance(MortgageLoanModel mortgage_loan, Long property_value) {
        float max_finance = mortgage_loan.getLoan_type().getMax_financed_percentage() * property_value;
        if (mortgage_loan.getFinanced_amount() > max_finance) {
            throw new IllegalArgumentException("Financed amount exceeded");
        }
    }

    private void validateMaxAge(MortgageLoanModel mortgage_loan, Date validated_birthdate) {
        int client_years = validated_birthdate.toLocalDate().getYear();
        int payment_term = mortgage_loan.getPayment_term();
        if (client_years + payment_term > 70) {
            throw new IllegalArgumentException("Client wont pay before jubilation");
        }
    }

    private void validateCredit(MortgageLoanModel mortgage_loan, CreditValidation credit_validation) {
        validateQuotaIncomeRelation(mortgage_loan, credit_validation.getClient_income());
        validateCreditHistory(credit_validation.getHas_acceptable_credit_history());
        validateFinancialStability(credit_validation.getHas_financial_stability());
        validateDebtIncomeRelation(mortgage_loan, credit_validation.getClient_income(), credit_validation.getMonthly_debt());
        validateMaxFinance(mortgage_loan, credit_validation.getProperty_value());
        validateMaxAge(mortgage_loan, credit_validation.getValidated_birthdate());
    }

    private int sumSavingsScore(MortgageLoanModel mortgage, SavingCapacity saving_capacity) {
        int total = 0;
        total += (checkMinimumBalance(mortgage,
                                      saving_capacity.getClient_balance()) ? 1 : 0);
        total += (saving_capacity.isHas_consistent_savings() ? 1 : 0);
        total += (saving_capacity.isHas_periodic_savings() ? 1 : 0);
        total += (checkBalanceLongevityRelation(mortgage,
                                                saving_capacity.getClient_balance(),
                                                saving_capacity.getSavings_account_longevity()) ? 1 : 0);
        total += (saving_capacity.is_financially_stable() ? 1 : 0);
        return total;
    }

    private boolean checkMinimumBalance(MortgageLoanModel mortgage_loan, Long balance) {
        float relation = (float) balance / mortgage_loan.getFinanced_amount();
        return !(relation > 0.1);
    }

    private boolean checkBalanceLongevityRelation(MortgageLoanModel mortgage_loan, Long balance, int longevity) {
        float relation = (float) balance / mortgage_loan.getFinanced_amount();
        if (longevity < 2) {
            return relation < 0.2;
        } else {
            return relation < 0.1;
        }
    }

    private void validateMortgageReview(Long mortgage_loan_id) {
        Optional<MortgageLoanReviewModel> mortgage = mortgageLoanReviewRepository.findById(mortgage_loan_id);
        if (mortgage.isEmpty()) {
            return;
        }

        if (!mortgage.get().isHas_been_reviewed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The mortgage loan needs to be reviewed before getting approved");
        }

        if (!mortgage.get().isHas_been_approved()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The mortgage loan needs to be approved by another executive");
        }
    }

    private SimpleResponse setRequiresReview(Long mortgage_loan_id, ExecutiveModel executive) {
        MortgageLoanModel mortgage = getMortgageLoan(mortgage_loan_id);
        MortgageLoanReviewModel review_request = new MortgageLoanReviewModel(mortgage, executive);
        mortgageLoanReviewRepository.save(review_request);
        return new SimpleResponse("Mortgage review requested");
    }

    private SimpleResponse reviewMortgage(Long mortgage_loan_id, MortgageReview review, ExecutiveModel executive) {
        MortgageLoanReviewModel mortgage = getMortgageForReviewal(mortgage_loan_id, executive);
        mortgage.setHas_been_reviewed(true);
        mortgage.setHas_been_approved(review.getIs_approved());
        mortgageLoanReviewRepository.save(mortgage);
        return new SimpleResponse("Review has been submited");
    }

    private MortgageLoanReviewModel getMortgageForReviewal(Long mortgage_loan_id, ExecutiveModel executive) {
        Optional<MortgageLoanReviewModel> mortgage_loan = mortgageLoanReviewRepository.findById(mortgage_loan_id);
        if (mortgage_loan.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The mortgage loan does not exist or does not require approval");
        }

        MortgageLoanReviewModel mortgage = mortgage_loan.get();
        ExecutiveModel requester = mortgage.getReview_requester();
        if (requester.getId().equals(executive.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not approve your own request");
        }

        return mortgage;
    }
}



