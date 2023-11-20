package com.service.login.auth.domain;

import com.service.login.auth.enums.CompanyType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


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

    @OneToOne
     Country registeredCountry;

    @ManyToOne
    Branch branch;
   private String name;
    private String domainName;

    private String currency;

    private Boolean isActive = true;

    private Boolean isVerified = false;

    CompanyType companyType = CompanyType.Regular;

    public Branch getHeadQuarterBranch() {
        return branches.stream()
                .filter(Branch::isHeadQuarter)
                .findFirst()
                .orElse(null);
    }

    public Boolean isIndividual() {
        return companyType == CompanyType.Individual;
    }
}
