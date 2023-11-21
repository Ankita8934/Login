package com.service.login.auth.domain;
import com.service.login.auth.enums.RegistrationType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.List;
import java.util.Map;


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
    public String uniqueId;
    public Boolean isActive = false;
    public boolean enabled = true;
    public boolean accountLocked = true;
    public Boolean isEditable = true;
    public Boolean acceptTOS = false;
   public String firstName;
   public String middleName;
   public String lastName;



    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    private String ipAddress;
    @ManyToOne
    private Branch branch;
    private String mobile;
    private String source = RegistrationType.EMAIL;



    public User(User employee, RoleGroup peopleRoleGroup, Role imprest, Role invoice, Role payroll, Role hire, Role vacation, Long loanRole, Long contractorRole, Role livechat, Role crm, Role myshop, Role esignature, Role mypayments, Role task, Role helpdesk, Role accounting, Role smartleads, Role taxes, Role mailer, Role ai) {
    }

    public User() {

    }

    public User(Map<String, Object> employee) {

    }


    public void setSource(RegistrationType registrationType) {
    }


    public Company getCompany() {
      return  branch.getCompany();
    }

    public User save(User employee) {
        return employee;
    }

    public String setFullName(String name) {
        if (name != null && name.contains(" ")) {
            String[] nameList = name.split(" ");
            firstName = nameList[0];
            if (nameList.length == 2) {
                lastName = nameList[1];
            } else if (nameList.length > 2) {
                middleName = nameList[1];
                lastName = nameList[2];
            }
        } else {
        }
        return firstName = name;

    }
}

