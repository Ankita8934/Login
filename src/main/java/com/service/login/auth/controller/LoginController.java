package com.service.login.auth.controller;
import com.service.login.auth.co.LoginCO;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.dto.TokenResponse;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.jwt.UserDetailsServiceImpl;
import com.service.login.auth.repo.UserRepository;
import com.service.login.auth.service.LoginService;
import com.service.login.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtTokenUtil jwtUtils;


    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @RequestMapping("/")
    public String home() {
        return "source";
    }

    @GetMapping("/oauth2/code/google")
    public String home(Model model, @AuthenticationPrincipal OAuth2User OAuth2User) {
        String name = OAuth2User.getAttribute("name");
        String email = OAuth2User.getAttribute("email");
        User user = userService.findByEmail(email);
        if(user == null){
            userService.save(email,name);
        }
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        return "index";
    }

    @GetMapping("/login/auth")
    public String login(HttpServletRequest request, Model m) {
        String source = request.getParameter("source");
        System.out.println("+++source:--"+source);
       m = addAttributes(m, source);
        return "login"; // Replace with the actual API response
    }

    @ModelAttribute
    public Model addAttributes(Model model, String value) {
        model.addAttribute("variable", value);
        return model;
    }

    @RequestMapping(path = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(LoginCO loginRequest, HttpServletRequest request) throws InvalidResponseException {
        String source = request.getParameter("source");
        System.out.println("+++source:--"+source);
        ResponseDTO responseDTO = new ResponseDTO();
//        String jwtToken = null;
//
//        if (!loginRequest.getUsername().isEmpty()) {
//            User user = userService.findByEmail(loginRequest.getUsername());
//            if (user != null) {
//                jwtToken = jwtUtils.generateTokenForUser(user.getEmail(), user.getPassword());
//            } else {
//                SignUpCO signUpRequest = new SignUpCO();
//                signUpRequest.setEmail(loginRequest.getUsername());
//                signUpRequest.setPassword(loginRequest.getPassword());
//                loginService.sinUp(signUpRequest);
//                user = userService.findByEmail(loginRequest.getUsername());
//                jwtToken = jwtUtils.generateTokenForUser(user.getEmail(), user.getPassword());
//            }
//            TokenResponse tokenResponse = new TokenResponse();
//            tokenResponse.setToken(jwtToken);
//            return ResponseEntity.ok(tokenResponse);
//        }
//        else {
//            responseDTO.setFailureResponse("INVALID_USER");
//            return ResponseEntity.ok(responseDTO);
//        }
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
                user.setAccess_token(jwtToken);
                userRepository.save(user);
                return ResponseEntity.ok(tokenResponse);
            }
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpCO signUpRequest) throws InvalidResponseException {
        ResponseDTO responseDTO = new ResponseDTO();
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            responseDTO.setFailureResponse("USERNAME_ALREADY_TAKEN");
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
        responseDTO = loginService.sinUp(signUpRequest);
        return ResponseEntity.ok(responseDTO);
    }
}
