package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="executive")
public class ExecutiveModel extends UserModel {
    private String name;
}
