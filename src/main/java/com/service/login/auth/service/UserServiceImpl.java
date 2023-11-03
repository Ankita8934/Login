package com.service.login.auth.service;
import com.service.login.auth.domain.User;
import com.service.login.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginServiceImpl userService;

    @Override
    public User findByUserNameAndPassword(String username, String password12) {
        String password = new BCryptPasswordEncoder().encode(password12);
        System.out.println(password);
        return userRepository.findByPasswordAndEmail(password, username).orElse(null);
    }

    @Override
    public User findByEmail(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return optionalUser.orElse(null);
    }

}
