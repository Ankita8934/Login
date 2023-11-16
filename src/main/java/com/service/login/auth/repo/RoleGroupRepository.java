package com.service.login.auth.repo;
import com.service.login.auth.domain.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {
    com.service.login.auth.domain.RoleGroup findByName(String peopleRoleGroup) ;
}
