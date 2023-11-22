package com.service.login.auth.service;

import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.TokenResponse;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginServiceImpl userService;

    @Autowired
    JwtTokenUtil jwtUtils;

    @Value("${module.crm.url}")
    String crmUrl;

    @Value("${module.task.url}")
    String taskUrl;


    @Override
    public User findByEmail(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return optionalUser.orElse(null);
    }

    @Override
    public User save(String username, String password) {
        return null;
    }

    @Override
    public String createdTargetedUrl(String source, String username, String scheme) {
        String url = "";
        if (source.equals("crm")) {
            url = crmUrl;
        } else if (source.equals("task")) {
            url = taskUrl;
        }
        url = url.startsWith("http") ? url : scheme + ":" + url;
        User employee = findByEmail(username);
        String userToken = employee.getAccessToken();
        if (userToken == null) {
            userToken = generateJwtToken(employee);
        }
        String accessToken = (userToken + employee.uniqueId);
        byte[] strBytes = accessToken.getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(strBytes);
        String encodedToken = new String(encodedBytes);
        String redirectUrl = url + "?accessToken=" + encodedToken + "&lang=" + employee.locale_code + "&returnUrl= ";
        return redirectUrl;
    }

    @Override
    public String generateJwtToken(User employee) {
        String jwtToken = jwtUtils.generateTokenForUser(employee.getEmail(), employee.getPassword());
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwtToken);
        employee.setAccessToken(jwtToken);
        userRepository.save(employee);
        return jwtToken;
    }
}
