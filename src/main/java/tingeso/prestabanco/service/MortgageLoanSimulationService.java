package tingeso.prestabanco.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tingeso.prestabanco.dto.MortgageSimulationRequest;
import tingeso.prestabanco.dto.SimulationResponse;
import tingeso.prestabanco.dto.SimulationStep;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.MortgageLoanModel;
import tingeso.prestabanco.model.PreApprovedMortgageLoanModel;
import tingeso.prestabanco.repository.LoanTypeRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class MortgageLoanSimulationService {

    @Value("${bank.quota_income_threshold}")
    float quotaIncomeThreshold;

    @Autowired
    LoanTypeRepository loanTypeRepository;

    public SimulationResponse simulate(MortgageSimulationRequest req, ClientModel client) {
        MortgageLoanModel mortgageLoanModel = new MortgageLoanModel();
        mortgageLoanModel.setClient(client);
        mortgageLoanModel.setFinanced_amount(req.getFinanced_amount());
        mortgageLoanModel.setInterest_rate(req.getInterest_rate());
        mortgageLoanModel.setPayment_term(req.getPayment_term());
        PreApprovedMortgageLoanModel preApprovedLoanModel = new PreApprovedMortgageLoanModel(mortgageLoanModel);
        List<SimulationStep> steps = getValidations(req, client);
        return new SimulationResponse(steps.isEmpty(), steps, preApprovedLoanModel);
    }

    private List<SimulationStep> getValidations(MortgageSimulationRequest req, ClientModel client) {
        List<SimulationStep> validations = new ArrayList<>();
        MortgageLoanModel mortgageLoanModel = new MortgageLoanModel();
        mortgageLoanModel.setClient(client);
        mortgageLoanModel.setFinanced_amount(req.getFinanced_amount());
        mortgageLoanModel.setInterest_rate(req.getInterest_rate());
        mortgageLoanModel.setPayment_term(req.getPayment_term());
        validations.add(validateQuotaIncomeRelation(mortgageLoanModel, req.getClient_income()));
        validations.add(validateDebtIncomeRelation(mortgageLoanModel, req.getClient_income(), req.getMonthly_debt()));
        validations.add(validateMaxFinance(mortgageLoanModel, req.getProperty_value()));
        validations.add(validateMaxAge(mortgageLoanModel, client.getBirth_date()));
        return validations;
    }


    private SimulationStep validateQuotaIncomeRelation(MortgageLoanModel mortgage, Long income) {
        Long monthly_quota = mortgage.getMonthlyQuota();
        float relation = (float) monthly_quota / income;
        if (relation < quotaIncomeThreshold) {
            return new SimulationStep("Relación ingresos-mensualidad", "La relación entre los ingresos y la cuota del préstamo es demasiado alta", false);
        }
        return new SimulationStep("Relación ingresos-mensualidad", "La relación entre los ingresos y la cuota del préstamo es suficientemente segura", true);
    }

    private SimulationStep validateDebtIncomeRelation(MortgageLoanModel mortgage, Long income, Long monthly_debt) {
        Long monthly_quota = mortgage.getMonthlyQuota();
        float relation = (float) (monthly_quota + monthly_debt) / income;
        if (relation < quotaIncomeThreshold) {
            return new SimulationStep("Relación ingresos-deuda", "La relación entre los ingresos y la deuda ya existente es demasiado alta", false);
        }
        return new SimulationStep("Relación ingresos-deuda", "La relación entre los ingresos y la deuda ya existente es suficientemente segura", true);
    }

    private SimulationStep validateMaxFinance(MortgageLoanModel mortgage_loan, Long property_value) {
        float max_finance = mortgage_loan.getLoan_type().getMax_financed_percentage() * property_value;
        if (mortgage_loan.getFinanced_amount() > max_finance) {
            return new SimulationStep("Máximo financiamiento", "El monto solicitado es superior al permitido para este tipo de préstamo", false);
        }
        return new SimulationStep("Máximo financiado", "El monto solicitado está dentro de lo permitido para el tipo de préstamo", true);
    }

    private SimulationStep validateMaxAge(MortgageLoanModel mortgage_loan, Date validated_birthdate) {
        int client_years = validated_birthdate.toLocalDate().getYear();
        int payment_term = mortgage_loan.getPayment_term();
        if (client_years + payment_term > 70) {
            return new SimulationStep("Edad de último pago", "El préstamo terminará de ser pagado a una edad muy avanzada del solicitante", false);
        }
        return new SimulationStep("Edad de último pago", "El préstamo no presenta complicaciones en las edades de pago", true);
    }
}
