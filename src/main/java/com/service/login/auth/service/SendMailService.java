package com.service.login.auth.service;

import com.service.login.auth.domain.User;
import com.service.login.auth.enums.RegistrationType;
import com.service.login.auth.exception.AppConstant;
import com.service.login.auth.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendMailService {

    @Autowired
    UserService userService;

    public void sendUserVerificationEmail(User employee, String password, String tempSource, Map<String, Object> emailContent, OAuth2User OAuth2User) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", OAuth2User.getAttribute("name"));
//        params.put("password", OAuth2User.getAttribute("password"));
//        params.put("username", OAuth2User.getAttribute("username"));
//
//        String template;
//        String subjectText = "Welcome to " + "ZYk" + "! Your account has been created successfully.";
//        List<String> toUserEmailTemp = new ArrayList<>();
//        toUserEmailTemp.add(employee.getEmail());
//
//        if (RegistrationType.google.equals(employee.getSource())) {
//            String emailDomain = StrUtil.domainFromEmail(employee.getEmail());
//            subjectText = "Welcome to " + "zyk"+ "! Your account has been created and activated successfully.";
//
//            if (emailDomain.toLowerCase().equals(AppConstant.restrictedDomains) && employee.getCompany().getIsActive() && !employee.getCompany().getIsVerified()) {
//                template = "/shared/emailTemplates/welcomeEmailCompanyAutoVerified";
//            } else {
//                template = "/shared/emailTemplates/welcomeEmail";
//            }
//        } else {
//            String verifyUrl;
//
//            if (employee.getSource() != null && emailContent != null) {
//
//                verifyUrl = uri.link(controller: 'employee', action: 'verify', params: [encryptEmail: employee.encryptEmail, source: tempSource], absolute: true)
//
//                subjectText = (String) emailContent.get("subject");
//                params.put("body", emailContent.get("body"));
//            } else {
//                verifyUrl = uri.link("employee", "verify", ("encryptEmail", employee.getEncryptEmail()), true);
//            }
//            params.put("verifyUrl", verifyUrl);
//            template = "/shared/emailTemplates/verificationEmail";
//        }
//        communicateViaApi("ZYK_COMMON", ,subjectText, AppConstant.ADMIN_EMAIL, toUserEmailTemp, null, template, params, employee.getSource());
//    }
//
//
//
//    public void communicateViaApi(String emailType, String subject, String senderEmailId, List<String> recipientEmailList, String emailBody, String pathToTemplate, Map<String, Object> modelForTemplate, RegistrationType communicationType, List<String> ccList, List<String> bccList, boolean sendSingleEmail) {
//        if (communicationType.equals(RegistrationType.Mobile)) {
//            try {
//                String mobileNumber = userService.findAllByEmailInList(recipientEmailList).get(0).getMobile();
//                sendMobileMessageService.sendSMSViaApi(mobileNumber, pathToTemplate);
//            } catch (Exception e) {
//                log.info("Setup sending mobile sms");
//            }
//        } else {
//            sendMailViaApi(emailType, subject, senderEmailId, recipientEmailList, emailBody, pathToTemplate, modelForTemplate, ccList, bccList, sendSingleEmail);
//        }
 }

}



