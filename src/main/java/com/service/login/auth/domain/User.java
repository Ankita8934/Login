package com.service.login.auth.domain;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;


@Entity
@Table(name = "employee")
@DynamicInsert
@DynamicUpdate
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String email;
    public String username;
    public String password;
    public String profilePicUrl;
    @Column(length = 10000)
    public String accessToken;
    public String locale_code = "en_US";
   public String firstName;
   public String middleName;
   public String lastName;
    public String uniqueId;
}

