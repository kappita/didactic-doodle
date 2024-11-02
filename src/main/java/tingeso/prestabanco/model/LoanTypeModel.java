package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "loan_type")
public class LoanTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer max_term;
    private Float min_interest_rate;
    private Float max_interest_rate;
    private Float max_financed_percentage;

    @ManyToMany
    @JoinTable(
            name = "loan_type_required_document",
            joinColumns = @JoinColumn(name = "loan_type_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id")
    )
    private List<DocumentTypeModel> required_documents;
}
