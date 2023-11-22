package com.service.login.auth.controller;

import com.service.login.auth.co.LoginCO;
import com.service.login.auth.co.ResponseEntity;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.co.SignUpResponse;
import com.service.login.auth.domain.*;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.exception.AppConstant;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.jwt.UserDetailsServiceImpl;
import com.service.login.auth.repo.*;
import com.service.login.auth.service.LoginService;
import com.service.login.auth.service.UserService;
import com.service.login.auth.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

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

    @GetMapping("/login")
    public String login() {
        return "login"; // Create a login page
    }

    @GetMapping("/oauth2/code/google")
    public void home(Model model, @AuthenticationPrincipal OAuth2User OAuth2User, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        ResponseDTO responseDTO = new ResponseDTO();
        String source = (String) httpSession.getAttribute("source");
        String targetUrl = "";
        String failureUrl = "/login/auth";
        String name = OAuth2User.getAttribute("name");
        String email = OAuth2User.getAttribute("email");
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        User user =userService.findByEmail(OAuth2User.getAttribute("email"));
        if(user==null){
                user = new User();
            user.setEmail(OAuth2User.getAttribute("email"));
            user.setUsername(OAuth2User.getAttribute("name"));
            user.setPassword(new BCryptPasswordEncoder().encode(StrUtil.randomPassword(8)));
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            String random= StrUtil.randomStr(alphabet,10);
            user.setUniqueId(random);
            userRepository.save(user);
            SignUpCO signUpCO = new SignUpCO();
            signUpCO.setUserName(OAuth2User.getAttribute("name"));
            signUpCO.setEmail(OAuth2User.getAttribute("email"));
            loginService.sending(signUpCO, OAuth2User.getAttribute("email"));
        }
        String jwtToken = userService.generateJwtToken(user);
        if (source != null && (source.equals("crm") || source.equals("task"))) {
            targetUrl = userService.createdTargetedUrl(source, user.getEmail(), request.getScheme());
            response.sendRedirect(targetUrl);
        }
    }

    @GetMapping("/login/auth")
    public String login(HttpServletRequest request, Model m, HttpSession httpSession) {
        String source = request.getParameter("source");
        String message = request.getParameter("error");
        httpSession.setAttribute("source", source);
        m = addAttributes(m, source);
        if (message != null && message.equals("true")) {
            m.addAttribute("message", "Sorry, we were not able to find a user with that username and password");
        }
        return "login";
    }

    @ModelAttribute
    public Model addAttributes(Model model, String value) {
        model.addAttribute("variable", value);
        return model;
    }

    @RequestMapping(path = "/auth", method = RequestMethod.POST)
    public void authenticateUser(LoginCO loginRequest, String source, HttpServletRequest request, HttpServletResponse response) throws InvalidResponseException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        String targetUrl = "";
        String failureUrl = "/login/auth";
        try {
            User user = userService.findByEmail(loginRequest.getUsername());
            if (user == null) {
                failureUrl += "?source=" + source + "&error=" + true;
                response.sendRedirect(request.getContextPath() + failureUrl);
            } else {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    responseDTO.setFailureResponse("INVALID_CREDENTIALS");
                    failureUrl += "?source=" + source + "&error=" + true;
                    response.sendRedirect(request.getContextPath() + failureUrl);
                } else {
                    String jwtToken = userService.generateJwtToken(user);
                    if (source != null && (source.equals("crm") || source.equals("task"))) {
                        targetUrl = userService.createdTargetedUrl(source, loginRequest.getUsername(), request.getScheme());
                        response.sendRedirect(targetUrl);
                    }
                }
            }
        } catch (BadCredentialsException e) {
            failureUrl += "?source=" + source + "&error=" + true;
            response.sendRedirect(request.getContextPath() + failureUrl);
        }
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        User user= new User();
        model.addAttribute("User",user);
        return "registration";
    }


@RequestMapping(path = "/signup", method = RequestMethod.POST)
    public String register(SignUpCO regRequest)
            throws Exception {
    ResponseDTO responseDTO = new ResponseDTO();
        ResponseEntity responseEntity = new ResponseEntity<>(loginService.register(regRequest), AppConstant.SUCCESS);
        return "thankyouPage";
    }

}

