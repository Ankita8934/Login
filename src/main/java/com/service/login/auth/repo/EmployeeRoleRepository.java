package com.service.login.auth.repo;

import com.service.login.auth.domain.EmployeeRole;
import com.service.login.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {

    EmployeeRole findByEmployee(User emp);
    EmployeeRole save(User employee);

}
