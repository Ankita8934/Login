package com.service.login.auth.domain;

import com.service.login.auth.enums.CompanyType;
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

    CompanyType companyType = CompanyType.Regular;

    public Branch getHeadQuarterBranch() {
        return branches.stream()
                .filter(Branch::isHeadQuarter)
                .findFirst()
                .orElse(null);
    }



    Boolean getIsIndividual() {
        return companyType == CompanyType.Individual;
    }
}
