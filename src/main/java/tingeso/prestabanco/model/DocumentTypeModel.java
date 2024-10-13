package tingeso.prestabanco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "document_type")
@Data
public class DocumentTypeModel {
    @Id
    private Long id;
    private String name;
    private String description;

}
