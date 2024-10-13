package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.MortgageLoanPendingDocumentationModel;

public interface MortgageLoanPendingDocumentationRepository extends JpaRepository<MortgageLoanPendingDocumentationModel, Long> {
}
