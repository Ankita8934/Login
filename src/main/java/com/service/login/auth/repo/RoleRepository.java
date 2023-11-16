package com.service.login.auth.repo;

import com.service.login.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String vacationRole);
    Optional<Role> findById(Long roleId);
}
