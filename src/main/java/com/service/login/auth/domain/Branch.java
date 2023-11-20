package com.service.login.auth.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Company company;

    @OneToMany
    List<User> employees;

    Boolean isHeadQuarter = false;

    public boolean isHeadQuarter() {
        return isHeadQuarter;
    }

    public void setHeadQuarter(Boolean headQuarter) {
        isHeadQuarter = headQuarter;
    }
}
