package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.LoanStatusModel;

public interface LoanStatusRepository extends JpaRepository<LoanStatusModel, String> {
}
