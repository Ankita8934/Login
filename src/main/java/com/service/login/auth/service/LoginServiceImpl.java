package com.service.login.auth.service;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.co.SignUpResponse;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.exception.AppConstant;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.repo.UserRepository;
import com.service.login.auth.utils.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.Map;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    public String sending(SignUpCO signUpRequest, String recipientEmail)
            throws MessagingException, IOException {

        Map<String, Object> model = new HashMap<>();
        model.put("Welcome", signUpRequest);
        sendSuccessEmail(recipientEmail, model);
        return recipientEmail;
    }

    @Override
    public void sendSuccessEmail(String recipientEmail, Map<String, Object> model)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(model);

        String html = templateEngine.process("welcomeMail", context);
        helper.setTo(recipientEmail);
        helper.setText(html, true);
        helper.setSubject("Welcome to Mira-AI-Development");

        helper.setFrom("vaishalibansal.rtu@gmail.com", "Mira-AI-Development");
        mailSender.send(message);
    }

    @Override
    public SignUpResponse register(SignUpCO regRequest) throws Exception {

        String firstname = regRequest.getUserName();
        String email = regRequest.getEmail();
        String password = regRequest.getPassword();
        String confirmPassword = regRequest.getConfirmPassword();
        if (!ObjectUtils.isEmpty(userRepository.findByEmail(regRequest.getEmail()))) {
            throw new InvalidResponseException(AppConstant.CUSTOMER_EMAIL_ALREADY_EXIT);
        }
        User customer = new User();
        customer.setEmail(regRequest.getEmail());
        customer.setUsername(regRequest.getUserName());
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String random= StrUtil.randomStr(alphabet,10);
        customer.setUniqueId(random);
        customer.setPassword(new BCryptPasswordEncoder().encode(regRequest.getPassword()));
        userRepository.save(customer);
        BeanUtils.copyProperties(regRequest, customer);

        SignUpResponse customerRegisterResponse = new SignUpResponse();
        BeanUtils.copyProperties(customer, customerRegisterResponse);
        sending(regRequest, regRequest.getEmail());
        return customerRegisterResponse;
    }
}
