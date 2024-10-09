package tingeso.prestabanco.model;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;


@Data
@Entity
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



}
