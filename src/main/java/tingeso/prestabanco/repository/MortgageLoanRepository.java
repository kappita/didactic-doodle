package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.MortgageLoanModel;

import java.util.List;

public interface MortgageLoanRepository extends JpaRepository<MortgageLoanModel, Long> {
    List<MortgageLoanModel> findAllByClientId(Long clientId);
}
