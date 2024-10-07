package tingeso.prestabanco.model;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="client")
public class ClientModel extends UserModel {
    private String name;
    private String last_name;
    private Date birth_date;
    private String gender;
    private String nationality;
    private String address;
    private String phone_number;



}
