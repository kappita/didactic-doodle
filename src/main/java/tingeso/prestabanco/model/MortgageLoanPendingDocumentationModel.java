package tingeso.prestabanco.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import tingeso.prestabanco.dto.PendingDocumentationRequest;
import tingeso.prestabanco.repository.DocumentTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mortgage_loan_pending_documentation")
public class MortgageLoanPendingDocumentationModel extends MortgageLoanModel{

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "pending_documentation",
            joinColumns = @JoinColumn(name = "mortgage_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id")
    )
    private List<DocumentTypeModel> missing_documents = new ArrayList<>();
    private String details;

    public MortgageLoanPendingDocumentationModel(MortgageLoanModel mortgage) {
        super(mortgage);
    }

    public void addMissingDocuments(List<DocumentTypeModel> docs) {
        missing_documents.addAll(docs);
    }

}
