package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.LoanTypeModel;

public interface LoanTypeRepository extends JpaRepository<LoanTypeModel, Long> {
}
