package com.service.login.auth.service;


import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.co.SignUpResponse;


import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface LoginService {
    SignUpResponse register(SignUpCO regRequest) throws Exception;
     void sendSuccessEmail(String recipientEmail, Map<String, Object> model) throws MessagingException, UnsupportedEncodingException;

     String sending(SignUpCO signUpRequest, String recipientEmail)
            throws MessagingException, IOException;
}

