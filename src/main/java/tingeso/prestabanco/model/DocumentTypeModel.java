package tingeso.prestabanco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_type")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentTypeModel {
    @Id
    private Long id;
    private String name;
    private String description;

}
