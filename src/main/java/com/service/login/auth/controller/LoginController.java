package com.service.login.auth.controller;
import com.service.login.auth.co.LoginCO;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.dto.TokenResponse;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtTokenUtil jwtUtils;


    @Autowired
    UserService userService;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginCO loginRequest) {
        ResponseDTO responseDTO = new ResponseDTO();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByEmail(loginRequest.getUsername());
        if (user == null) {
            responseDTO.setFailureResponse("USER_NOT_EXIST");
            return ResponseEntity.ok(responseDTO);
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                responseDTO.setFailureResponse("INVALID_CREDENTIALS");
                return ResponseEntity.ok(responseDTO);
            } else {
                String jwtToken = jwtUtils.generateTokenForUser(user.getEmail(), user.getPassword());
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(jwtToken);
                return ResponseEntity.ok(tokenResponse);
            }
        }
    }

}
