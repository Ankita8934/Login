package com.service.login.auth.domain;
import com.service.login.auth.enums.RegistrationType;
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
    public String password;
    @Column(length = 10000)
    public String accessToken;
    public String locale_code = "en_US";
    public String uniqueId;
    public Boolean isActive = false;
    public boolean enabled = true;
    public boolean accountLocked = true;
    public Boolean isEditable = true;
    public Boolean acceptTOS = false;
    private String ipAddress;
    @ManyToOne
    private Branch branch;
    private String mobile;



    public void setSource(RegistrationType registrationType) {
    }


    public Company getCompany() {
      return  branch.getCompany();
    }

    public User save(User employee) {
        return employee;
    }
}

