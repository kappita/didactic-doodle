package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.ExecutiveModel;

import java.util.Optional;

public interface ExecutiveRepository extends JpaRepository<ExecutiveModel, Long> {
    Optional<ExecutiveModel> findByEmail(String email);
}
