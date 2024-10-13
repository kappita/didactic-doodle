package tingeso.prestabanco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "loan_status")
public class LoanStatusModel {
    @Id
    private String id;
    private String name;
    private String description;
}
