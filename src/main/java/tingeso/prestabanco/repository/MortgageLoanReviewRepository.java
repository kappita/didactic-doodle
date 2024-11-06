package tingeso.prestabanco.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso.prestabanco.model.MortgageLoanReviewModel;

public interface MortgageLoanReviewRepository extends JpaRepository<MortgageLoanReviewModel, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM mortgage_loan_review WHERE id = :id", nativeQuery = true)
    void deleteReview(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO mortgage_loan_review (id, review_requester_id, has_been_reviewed, has_been_approved) VALUES (:id, :requester_id, false, false)", nativeQuery = true)
    void addReview(@Param("id") Long id, @Param("requester_id") Long requester_id);

}
