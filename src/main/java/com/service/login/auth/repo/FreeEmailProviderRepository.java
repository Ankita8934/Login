package com.service.login.auth.repo;

import com.service.login.auth.domain.FreeEmailProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreeEmailProviderRepository  extends JpaRepository<FreeEmailProvider, Long> {

    List<String> findDomainNameByIsActiveIsTrue();
}
