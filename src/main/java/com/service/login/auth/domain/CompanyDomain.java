package com.service.login.auth.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "company_domain", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CompanyDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne
    private Company company;

}
