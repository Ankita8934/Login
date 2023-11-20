package com.service.login.auth.service;

import com.service.login.auth.domain.User;
import com.service.login.auth.enums.RegistrationType;
import com.service.login.auth.exception.AppConstant;
import com.service.login.auth.utils.StrUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMailService {

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
//                String controller = "employee";
//                String action = "verify";
//                String encryptEmail = "encryptedEmail123";
//
//                String uri = UriComponentsBuilder
//                        .newInstance()
//                        .pathSegment(controller)
//                        .pathSegment(action)
//                        .queryParam("encryptEmail", encryptEmail)
//                        .build()
//                        .toUriString();
//
////                verifyUrl = uri.link("employee", "verify", Map.of("encryptEmail", employee.getEncryptEmail(), "source", tempSource), true);
////                subjectText = (String) emailContent.get("subject");
//                params.put("body", emailContent.get("body"));
//            } else {
//                verifyUrl = grailsLinkGenerator.link("employee", "verify", Map.of("encryptEmail", employee.getEncryptEmail()), true);
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
//                String mobileNumber = User.findAllByEmailInList(recipientEmailList).get(0).getMobile();
//                sendMobileMessageService.sendSMSViaApi(mobileNumber, pathToTemplate);
//            } catch (Exception e) {
//                log.info("Setup sending mobile sms");
//            }
//        } else {
//            sendMailViaApi(emailType, subject, senderEmailId, recipientEmailList, emailBody, pathToTemplate, modelForTemplate, ccList, bccList, sendSingleEmail);
//        }
  }

}



