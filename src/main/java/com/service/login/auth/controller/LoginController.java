package com.service.login.auth.controller;

import com.service.login.auth.co.LoginCO;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.CompanyDomain;
import com.service.login.auth.domain.Role;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.enums.RegistrationType;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.jwt.UserDetailsServiceImpl;
import com.service.login.auth.repo.CompanyDomainRepository;
import com.service.login.auth.repo.FreeEmailProviderRepository;
import com.service.login.auth.repo.UserRepository;
import com.service.login.auth.service.LoginService;
import com.service.login.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    @Autowired
    FreeEmailProviderRepository freeEmailProviderRepository;

    @Autowired
    CompanyDomainRepository companyDomainRepository;

    @RequestMapping("/")
    public String home() {
        return "source";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Create a login page
    }

    @GetMapping("/oauth2/code/google")
    public void home(Model model, @AuthenticationPrincipal OAuth2User OAuth2User, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        String source = (String) httpSession.getAttribute("source");
        String targetUrl = "";
        String failureUrl = "/login/auth";
        String name = OAuth2User.getAttribute("name");
        String email = OAuth2User.getAttribute("email");
//        source=request.getParameter("source");
        model.addAttribute("name", name);
        model.addAttribute("email", email);

        User user = userService.findByEmail(OAuth2User.getAttribute("email"));
        if (user == null) {
            failureUrl += "?source=" + source + "&error=" + true;
            response.sendRedirect(request.getContextPath() + failureUrl);
        } else {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            if (!encoder.matches(user.getPassword(), user.getPassword())) {
//                responseDTO.setFailureResponse("INVALID_CREDENTIALS");
//                failureUrl += "?source=" + source + "&error=" + true;
//                response.sendRedirect(request.getContextPath() + failureUrl);
//            } else {
            String jwtToken = userService.generateJwtToken(user);
            if (source != null && (source.equals("crm") || source.equals("task"))) {
                targetUrl = userService.createdTargetedUrl(source, user.getEmail(), request.getScheme());
                response.sendRedirect(targetUrl);
            }
//            }

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
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
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

    public void loginViaGoogle(String source, HttpServletRequest request) {
//        if (resp.status.toString() == '200') {
//            String email = resp.body.email;
//            User employee = User.findByEmail(email);
//            Boolean register = (employee == null) ? true : false;
//            employee = userService.upsert(resp.body);
//            if (employee instanceof User) {
//                if (!employee.isActive) {
//                }
//                if (!employee.enabled) {
//                }
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(employee.getEmail(), employee.getPassword()));
//                userService.generateJwtToken(employee);
//                if (source != null && (source.equals("crm") || source.equals("task"))) {
//                    userService.createdTargetedUrl(source, employee.getEmail(), request.getScheme());
//                } else {
//                    if (register) {
//                        flash.message = 'register'
//                    }
//                    render request.scheme + ":" + AppConstant.APP_PARENT_URL
//                }
//            } else {
//                render "Login failed! " + employee
//            }
//            return
//        } else {
//            render "Login failed! Invalid auth token!"
//            return
//        }


    }


    public String loginViaGoogle(@AuthenticationPrincipal OAuth2User OAuth2User, @RequestParam("code") String code, @RequestParam(value = "source") String source, HttpServletRequest request, Model model) {

        if (OAuth2User != null) {
            String email = OAuth2User.getAttribute("email");
            User user = userService.findByEmail(email);
            boolean register = (user == null);
            upsert(OAuth2User, source, request);
            if (user != null) {
                if (!user.isActive) {
                    model.addAttribute("message", "Sorry, your account is inactive. Please contact your company admin.");
                    return "errorMessage";
                }
                if (!user.isEnabled()) {
                    model.addAttribute("message", "Sorry, your account is disabled. Please contact your company admin.");
                    return "errorMessage";
                }


            }


        }
        return null;

    }

    void upsert (OAuth2User OAuth2User, String source, HttpServletRequest request){
        String email = OAuth2User.getAttribute("email");
        User user = userService.findByEmail(email);

        if (user == null) {
            setupCompanyAndEmployee(OAuth2User, source, request);
            user = userService.findByEmail(email);
        } else {
            if (user.isAccountLocked() || !user.isEditable || !user.acceptTOS) {
                user.setAccountLocked(false);
                user.setIsEditable(true);
                user.setAcceptTOS(true);
            }
//            saveEmployeeInfo(user);
        }
    }



    void setupCompanyAndEmployee(OAuth2User OAuth2User, String source, HttpServletRequest request){
        String email = OAuth2User.getAttribute("email");

        // Fetch domain from email received
        String companyName = (email.split("@")[1]).split(".")[0].toLowerCase();
        if (companyName != null) {
            registerEmployeeAndCompany(companyName, OAuth2User, source, request);
        }
    }

    void registerEmployeeAndCompany(String companyName, OAuth2User OAuth2User, String source, HttpServletRequest request) {
        String companyType = "Regular";
        String ipAddress = request.getRemoteAddr();

        // Check if company requested belongs to restricted domain group
        if (freeEmailProviderRepository.findDomainNameByIsActiveIsTrue().contains(companyName.toLowerCase())) {
            companyName = OAuth2User.getAttribute("email").toString().split("@")[0] + "_business";
            companyType = "Individual";
        }

        // Check if company domain already existed
        CompanyDomain companyDomain = companyDomainRepository.findByName(companyName);
        if (companyDomain != null) {
            User employee = new User();
            employee.setBranch(companyDomain.getCompany().getHeadQuarterBranch());
            employee.acceptTOS = true;
            employee.isEditable = true;
            employee.isActive = true;
            employee.setIpAddress(ipAddress);
            if (companyType == "Individual") {
                createEmployee(employee, "employee", source, Role.externalVacationEmployeeRole.authority, Role.externalPayrollEmployeeRole.authority, Role.externalExpenseEmployeeRole.authority, Role.externalInvoiceEmployeeRole.authority, Role.externalRecruitEmployeeRole.authority, Role.externalLivechatEmployeeRole.authority, Role.externalCrmEmployeeRole.authority, Role.externalMyshopEmployeeRole.authority, Role.externalEsignatureEmployeeRole.authority, Role.externalMypaymentsEmployeeRole.authority, Role.externalTaskEmployeeRole.authority, Role.externalHelpdeskEmployeeRole.authority, Role.externalAccountingEmployeeRole.authority, Role.externalSmartleadsEmployeeRole.authority, Role.externalTaxesEmployeeRole.authority, Role.externalMailerEmployeeRole.authority, params.registrationType as RegistrationType)
            } else {
                createEmployee(employee, "employee", source, params.emailSubjectAndBody ? params.emailSubjectAndBody : [:], "ROLE_VACATION_COMPANY_BRANCH_EMPLOYEE", "ROLE_PAYROLL_COMPANY_BRANCH_EMPLOYEE", "ROLE_EXPENSE_COMPANY_BRANCH_EMPLOYEE", "ROLE_INVOICE_COMPANY_BRANCH_EMPLOYEE", "ROLE_RECRUIT_USER", "ROLE_LIVECHAT_COMPANY_BRANCH_EMPLOYEE", "ROLE_CRM_COMPANY_BRANCH_EMPLOYEE", "ROLE_MYSHOP_COMPANY_BRANCH_EMPLOYEE", "ROLE_ESIGNATURE_COMPANY_BRANCH_EMPLOYEE", "ROLE_MYPAYMENTS_COMPANY_BRANCH_EMPLOYEE", "ROLE_TASK_COMPANY_BRANCH_EMPLOYEE", "ROLE_HELPDESK_COMPANY_BRANCH_EMPLOYEE", "ROLE_ACCOUNTING_COMPANY_BRANCH_EMPLOYEE", "ROLE_SMARTLEADS_COMPANY_BRANCH_EMPLOYEE", "ROLE_TAXES_COMPANY_BRANCH_EMPLOYEE", "ROLE_MAILER_COMPANY_BRANCH_EMPLOYEE", "ROLE_AI_COMPANY_BRANCH_EMPLOYEE", params.registrationType as RegistrationType)
            }
        } else {
            params.company = [name: companyName, domainName: companyName, companyType: companyType]
            params.branch = [name: "Head Quarters"]

            companyService.createCompanyBranchAndEmployee(params, AppConstant.COMPANY_ADMIN, params.inviteFlag ? true : false);
        }
    }

}
