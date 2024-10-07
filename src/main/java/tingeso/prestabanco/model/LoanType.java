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
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer max_term;
    private Float min_interest_rate;
    private Float max_interest_rate;
    private Float max_financed_percentage;
    @ElementCollection
    private List<String> required_documents;
}
