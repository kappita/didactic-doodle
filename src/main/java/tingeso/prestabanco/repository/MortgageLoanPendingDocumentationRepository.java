package tingeso.prestabanco.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso.prestabanco.model.MortgageLoanPendingDocumentationModel;

import java.util.List;

public interface MortgageLoanPendingDocumentationRepository extends JpaRepository<MortgageLoanPendingDocumentationModel, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO pending_documentation (mortgage_id, document_type_id) VALUES (:mortgage_id, :document_type_id) ON CONFLICT (mortgage_id, document_type_id) DO NOTHING ", nativeQuery = true)
    void insertDocumentation(@Param("mortgage_id")Long mortgage_id, @Param("document_type_id") Long document_type_id);

    @Modifying
    @Query(value = "DELETE FROM pending_documentation WHERE mortgage_id = :mortgage_id", nativeQuery = true)
    void deleteDocumentation(@Param("mortgage_id")Long mortgage_id);
}
