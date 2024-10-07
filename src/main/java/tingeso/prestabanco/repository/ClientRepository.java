package tingeso.prestabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tingeso.prestabanco.model.ClientModel;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {
    Optional<ClientModel> findByEmail(String email);


}
