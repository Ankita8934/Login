package com.service.login.auth.repo;

import com.service.login.auth.domain.CompanyDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDomainRepository extends JpaRepository<CompanyDomain, Long> {

    CompanyDomain findByName(String name);
    CompanyDomain save(String company);

}
