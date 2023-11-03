package com.service.login.auth.service;


import com.service.login.auth.domain.User;

import java.util.Optional;

public interface UserService {

    User findByUserNameAndPassword(String username, String password);

   User findByEmail(String username);

}
