package com.service.login.auth.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    List<Branch> branches;

    @OneToMany
    List<CompanyDomain> companyDomain;

    public Branch getHeadQuarterBranch() {
        return branches.stream()
                .filter(Branch::isHeadQuarter)
                .findFirst()
                .orElse(null);
    }
}
