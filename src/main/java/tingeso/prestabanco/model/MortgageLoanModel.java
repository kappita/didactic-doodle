package tingeso.prestabanco.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import tingeso.prestabanco.dto.AddDocumentsRequest;
import tingeso.prestabanco.dto.MortgageLoanRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.pow;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
//        @JsonSubTypes.Type(value = MortgageLoanPendingDocumentationModel.class, name = "Pending documentation"),
        @JsonSubTypes.Type(value = PreApprovedMortgageLoanModel.class, name = "Pre Approved")
})

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Builder(toBuilder = true)
@Entity(name = "mortgage_loan")
public class MortgageLoanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    protected ClientModel client;
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    protected LoanTypeModel loan_type;
    protected Integer payment_term;
    protected Long financed_amount;
    protected Float interest_rate;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "mortgage_document",
            joinColumns = @JoinColumn(name = "mortgage_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    protected List<DocumentModel> documents;

    @ManyToOne
    @JoinColumn(name = "status_id")
    protected LoanStatusModel status;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "pending_documentation",
            joinColumns = @JoinColumn(name = "mortgage_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id")
    )
    private List<DocumentTypeModel> missing_documents;

    public MortgageLoanModel(MortgageLoanRequest request,
                             ClientModel client,
                             LoanStatusModel status,
                             LoanTypeModel loan_type) throws IllegalArgumentException {
        this.client = client;
        this.status = status;
        this.payment_term = request.getPayment_term();
        this.financed_amount = request.getFinanced_amount();
        this.interest_rate = request.getInterest_rate();
        this.documents = new ArrayList<>();

        this.loan_type = loan_type;
        validateSpecifications();

    }

    public MortgageLoanModel(MortgageLoanModel mortgage) {
        this.id = mortgage.getId();
        this.client = mortgage.getClient();
        this.status = mortgage.getStatus();
        this.payment_term = mortgage.getPayment_term();
        this.financed_amount = mortgage.getFinanced_amount();
        this.interest_rate = mortgage.getInterest_rate();
        this.documents = mortgage.getDocuments();
        this.loan_type = mortgage.getLoan_type();

    }

    public void addDocuments(MultipartFile[] docs) {
        try {
            for (MultipartFile file : docs) {
                DocumentModel document = new DocumentModel();
                document.setName(file.getOriginalFilename());
                document.setData(file.getBytes());
                this.documents.add(document);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Must send valid documents");
        }
    }

    public Long getMonthlyQuota() {
        Long p = this.financed_amount;
        float r = this.interest_rate / 12 / 100;
        int n = this.payment_term * 12;
        Double quota = p * ((r * pow(1 + r, n)) / (pow(1 + r, n) - 1));
        System.out.println(quota);
        return quota.longValue();
    }

    private void validateSpecifications() throws IllegalArgumentException {
        validateInterestRate();
        validateTerm();
    }

    private void validateInterestRate() throws IllegalArgumentException {
        if (interest_rate < loan_type.getMin_interest_rate()) {
            throw new IllegalArgumentException("Interest rate not valid");
        }

        if (interest_rate > loan_type.getMax_interest_rate()) {
            throw new IllegalArgumentException("Interest rate not valid");
        }

    }

    private void validateTerm() throws IllegalArgumentException {
        if (payment_term > loan_type.getMax_term()) {
            throw new IllegalArgumentException("Payment term exceeds max term");
        }
    }


    @Override
    public String toString() {
        return "MortgageLoanModel{" +
                "id=" + id +
                ", client=" + client +
                ", loan_type=" + loan_type +
                ", payment_term=" + payment_term +
                ", financed_amount=" + financed_amount +
                ", interest_rate=" + interest_rate +
                ", documents=" + documents +
                ", status=" + status +
                '}';
    }
}
