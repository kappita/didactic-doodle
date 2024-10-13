package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.MortgageLoanReviewModel;

public interface MortgageLoanReviewRepository extends JpaRepository<MortgageLoanReviewModel, Long> {
}
