package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Entity
@Table(name = "document")
@Data
@ToString
public class DocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    @ManyToOne
//    @JoinColumn(name = "document_type_id")
//    private DocumentTypeModel type;
    private byte[] data;
}
