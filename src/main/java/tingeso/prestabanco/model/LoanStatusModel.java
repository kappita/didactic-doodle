package tingeso.prestabanco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "loan_status")
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatusModel {
    @Id
    private String id;
    private String name;
    private String description;
}
