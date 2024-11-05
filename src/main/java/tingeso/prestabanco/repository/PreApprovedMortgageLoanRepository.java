package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.PreApprovedMortgageLoanModel;

public interface PreApprovedMortgageLoanRepository extends JpaRepository<PreApprovedMortgageLoanModel, Long> {

}
