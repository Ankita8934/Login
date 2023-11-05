package com.service.login.auth.service;

import com.service.login.auth.domain.User;

public interface UserService {

   User findByEmail(String username);

}
