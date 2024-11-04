package tingeso.prestabanco.model;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name="client")
public class ClientModel extends UserModel {
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "birth_date")
    private Date birth_date;
    @Column(name = "gender")
    private String gender;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String phone_number;

    public ClientModel(UserModel user, String name, String last_name, Date birth_date, String gender, String nationality, String address, String phone_number) {
        super(user);
        this.name = name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.nationality = nationality;
        this.address = address;
        this.phone_number = phone_number;
    }



}
