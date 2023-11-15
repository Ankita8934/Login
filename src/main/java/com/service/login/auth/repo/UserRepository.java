package com.service.login.auth.repo;

import com.service.login.auth.domain.Role;
import com.service.login.auth.domain.RoleGroup;
import com.service.login.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String username);

    RoleGroup findByName(String peopleRoleGroup) ;

    Role findByAuthority(String vacationRole);

}
