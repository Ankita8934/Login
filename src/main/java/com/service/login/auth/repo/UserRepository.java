package com.service.login.auth.repo;

import com.service.login.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPasswordAndEmail(String password, String email);

    Optional<User> findByEmail(String email);


    Optional<User> findByUsername(String username);
}
