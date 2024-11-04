package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString
@Entity
@Table(name = "executive")
@NoArgsConstructor
public class ExecutiveModel extends UserModel {
    @Column(name = "name")
    private String name;

    public ExecutiveModel(UserModel user, String name) {
        super(user);
        this.name = name;
    }

}
