package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.DocumentType;
import tingeso.prestabanco.model.DocumentTypeModel;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeModel, Long> {
    public List<DocumentTypeModel> findAllByIdIn(List<Long> ids);
}
