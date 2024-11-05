package tingeso.prestabanco.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tingeso.prestabanco.model.MortgageLoanReviewModel;

public interface MortgageLoanReviewRepository extends JpaRepository<MortgageLoanReviewModel, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM MortgageLoanReviewModel WHERE id = :id")
    void deleteReview(Long id);

}
