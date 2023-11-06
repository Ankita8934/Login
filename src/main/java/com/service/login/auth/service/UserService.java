package com.service.login.auth.service;

import com.service.login.auth.domain.User;
import com.service.login.auth.dto.TokenResponse;

public interface UserService {

   User findByEmail(String username);
 User save(String username,String password);

}
