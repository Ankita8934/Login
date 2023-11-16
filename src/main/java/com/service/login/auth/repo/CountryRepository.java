package com.service.login.auth.repo;

import com.service.login.auth.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findByName(String countryName);
}
