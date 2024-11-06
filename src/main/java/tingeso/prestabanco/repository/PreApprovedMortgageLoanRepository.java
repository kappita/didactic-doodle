package tingeso.prestabanco.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso.prestabanco.model.PreApprovedMortgageLoanModel;

public interface PreApprovedMortgageLoanRepository extends JpaRepository<PreApprovedMortgageLoanModel, Long> {

    @Transactional
    @Modifying
    @Query(value = "insert into preapproved_mortgage_loan (id, administration_fee, credit_insurance_fee, fire_insurance_fee, total_monthly_cost, total_cost) VALUES (:#{#e.id}, :#{#e.administration_fee}, :#{#e.credit_insurance_fee}, :#{#e.fire_insurance_fee}, :#{#e.total_monthly_cost}, :#{#e.total_cost})", nativeQuery = true)
    void preApprove(@Param("e") PreApprovedMortgageLoanModel preApprovedMortgageLoan);
}
