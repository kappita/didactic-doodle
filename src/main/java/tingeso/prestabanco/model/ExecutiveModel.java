package tingeso.prestabanco.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString
@Entity
@Table(name = "executive")
public class ExecutiveModel extends UserModel {
    @Column(name = "name")
    private String name;

}
