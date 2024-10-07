package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "mortgage_loan")
public class MortgageLoanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientModel client;
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loan_type;
    private Integer payment_term;
    private Long financed_amount;
    private Float interest_rate;
    @ElementCollection
    private List<File> documents;
}
