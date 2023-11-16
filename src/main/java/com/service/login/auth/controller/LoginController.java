package com.service.login.auth.controller;

import ch.qos.logback.classic.Logger;
import com.service.login.auth.co.LoginCO;
import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.*;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.enums.RegistrationType;
import com.service.login.auth.exception.InvalidResponseException;
import com.service.login.auth.jwt.JwtTokenUtil;
import com.service.login.auth.jwt.UserDetailsServiceImpl;
import com.service.login.auth.repo.*;
import com.service.login.auth.service.ApiService;
import com.service.login.auth.service.ApiServiceImpl;
import com.service.login.auth.service.LoginService;
import com.service.login.auth.service.UserService;
import com.service.login.auth.utils.StrUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

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

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleGroupRepository roleGroupRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ApiService apiService;

    @Autowired
    CountryRepository countryRepository;

    private RestTemplate restTemplate;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ApiServiceImpl.class);


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


    public String loginViaGoogle(@AuthenticationPrincipal OAuth2User OAuth2User, @RequestParam("code") String code, @RequestParam(value = "source") String source, HttpServletRequest request, Model model, Map params) {

        if (OAuth2User != null) {
            String email = OAuth2User.getAttribute("email");
            User user = userService.findByEmail(email);
            boolean register = (user == null);
            upsert(OAuth2User, source, request, params);
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

    void upsert(OAuth2User OAuth2User, String source, HttpServletRequest request, Map params) {
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


    void setupCompanyAndEmployee(OAuth2User OAuth2User, String source, HttpServletRequest request) {
        String email = OAuth2User.getAttribute("email");
        Map<String, Object> params = new HashMap<>();

        String companyName = (email.split("@")[1]).split(".")[0].toLowerCase();
        if (companyName != null) {
            Map<String, Object> employeeParams = new HashMap<>();
            employeeParams.put("username", OAuth2User.getAttribute("email"));
            employeeParams.put("email", OAuth2User.getAttribute("email"));
            employeeParams.put("profilePicUrl", OAuth2User.getAttribute("picture"));
            employeeParams.put("fullName", OAuth2User.getAttribute("name"));
            employeeParams.put("source", RegistrationType.google);
            employeeParams.put("isActive", true);
            params.put("employee", employeeParams);
            params.put("registrationType", RegistrationType.google);
            registerEmployeeAndCompany(companyName, OAuth2User, request,params);
        }
    }


    void registerEmployeeAndCompany(String companyName, OAuth2User OAuth2User, HttpServletRequest request,Map params) {
        String source = (String) params.get("source");
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
            User employee = new User((Map<String, Object>) params.get("employee"));
            employee.setBranch(companyDomain.getCompany().getHeadQuarterBranch());
            employee.acceptTOS = true;
            employee.isEditable = true;
            employee.isActive = true;
            employee.setIpAddress(ipAddress);

            if ("Individual".equals(companyType)) {
                Map<String, Object> emailSubjectAndBody =  (params != null && params.get("EmailSubjectAndBody") != null)
                        ? (Map<String, Object>) params.get("EmailSubjectAndBody")
                        : Collections.emptyMap();
                createEmployee(employee, "employee",source, emailSubjectAndBody,
                        (roleRepository.findById(Role.getExternalVacationEmployee())).get().getAuthority(),(roleRepository.findById(Role.getExternalPayrollEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getExpenseEmployee())).get().getAuthority(), (roleRepository.findById(Role.getInvoiceEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getRecruitEmployee())).get().getAuthority(), (roleRepository.findById(Role.getExternalLivechatEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getCrmEmployee())).get().getAuthority(), (roleRepository.findById(Role.getMyshopEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getEsignatureEmployee())).get().getAuthority(), (roleRepository.findById(Role.getMypaymentsEmployee())).get().getAuthority(),
                       ( roleRepository.findById(Role.getTaskEmployee())).get().getAuthority(), (roleRepository.findById(Role.getHelpdeskEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getAccountingEmployee())).get().getAuthority(), (roleRepository.findById(Role.getSmartleadsEmployee())).get().getAuthority(),
                        (roleRepository.findById(Role.getTaxesEmployee())).get().getAuthority(),(roleRepository.findById(Role.getMailerEmployee())).get().getAuthority(),(RegistrationType) params.get("registrationType"));
            } else {
                createEmployee(employee, "employee",source,  new HashMap<>(),
                        "ROLE_VACATION_COMPANY_BRANCH_EMPLOYEE", "ROLE_PAYROLL_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_EXPENSE_COMPANY_BRANCH_EMPLOYEE", "ROLE_INVOICE_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_RECRUIT_USER", "ROLE_LIVECHAT_COMPANY_BRANCH_EMPLOYEE", "ROLE_CRM_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_MYSHOP_COMPANY_BRANCH_EMPLOYEE", "ROLE_ESIGNATURE_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_MYPAYMENTS_COMPANY_BRANCH_EMPLOYEE", "ROLE_TASK_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_HELPDESK_COMPANY_BRANCH_EMPLOYEE", "ROLE_ACCOUNTING_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_SMARTLEADS_COMPANY_BRANCH_EMPLOYEE", "ROLE_TAXES_COMPANY_BRANCH_EMPLOYEE",
                        "ROLE_MAILER_COMPANY_BRANCH_EMPLOYEE", "ROLE_AI_COMPANY_BRANCH_EMPLOYEE",(RegistrationType) params.get("registrationType"));
            }
        } else {
            String finalCompanyName = companyName;
            Map<String, String> companyMap = new HashMap<String, String>() {{
                put("name", finalCompanyName);
                put("domainName", finalCompanyName);
                put("companyType", finalCompanyName);
            }};
            Map<String, String> branchMap = new HashMap<String, String>() {{
                put("uid", "Head Quarters");
            }};
            params.put("company", companyMap);
            params.put("branch", branchMap);

            createCompanyBranchAndEmployee(params,"COMPANY_ADMIN", params.containsKey("inviteFlag") ? (boolean) params.get("inviteFlag") : false);
        }

    }

    private void createEmployee(User employee, String employee1, String source, Map<String, Object> emailSubjectAndBody, String authority, String authority1, String authority2, String authority3, String authority4, String authority5, String authority6, String authority7, String authority8, String authority9, String authority10, String authority11, String authority12, String authority13, String authority14, String authority15, RegistrationType registrationType) {
    }


    private String createCompanyBranchAndEmployee(Map<String, Object> params, String roleGroup, boolean flagApi) {
        String email = (String) params.get("employee.email");
        User employee = userService.findByEmail(email);

        if (employee == null) {
            Company company = new Company();
            try {
                Map<String, Object> countryResp = apiService.fetchCountry();
                String countryName = (String) countryResp.get("name");
                String currencyCode = (String) countryResp.get("currencyCode");
                String isoCode = (String) countryResp.get("isoCode");

                Country country = countryRepository.findByName(countryName);
                if (country == null) {
                    country = new Country(countryName, isoCode);
                    countryRepository.save(country);
                }

                company.setRegisteredCountry((List<Country>) country);
                company.setCurrency(currencyCode);
            } catch (Exception e) {
                return "We are unable to trace your location. Please try again!";
            }

//            if (!company.validate()) {
//                // Handle validation errors or return validation errors
//                return company.retrieveErrors();
//            }
//
//            company.addToCompanyDomain(new CompanyDomain((String) params.get("company.domainName")));
//
//            Branch branch = new Branch((Map<String, Object>) params.get("branch"));
//            branch.setHeadQuarter(true);

            employee = new User((Map<String, Object>) params.get("employee"));
            employee.setAcceptTOS(true);
//            employee.setEditable(true);
//            employee.setActive(true);
            employee.setIpAddress(apiService.fetchIPAddress());


            companyRepository.save(company);
//            String emailDomain = StrUtil.domainFromEmail(employee.getEmail());
//            if (emailDomain.toLowerCase().contains(AppConstant.restrictedDomains) &&
//                    (params.get("employee.source") instanceof RegistrationType &&
//                            ((RegistrationType) params.get("employee.source")).in([RegistrationType.mobile, RegistrationType.google]))) {
//                automaticCompanyVerification(company);
//            }

            if (flagApi) {
                Map<String, Object> emailSubjectAndBody = (Map<String, Object>) params.get("emailSubjectAndBody");
               createEmployee(employee, roleGroup, (String) params.get("source"), emailSubjectAndBody,
                        "ROLE_VACATION_COMPANY_ADMIN", "ROLE_PAYROLL_COMPANY_ADMIN", "ROLE_EXPENSE_COMPANY_ADMIN",
                        "ROLE_INVOICE_COMPANY_ADMIN", "ROLE_RECRUIT_USER", "ROLE_LIVECHAT_COMPANY_ADMIN",
                        "ROLE_CRM_COMPANY_ADMIN", "ROLE_MYSHOP_COMPANY_ADMIN", "ROLE_ESIGNATURE_COMPANY_ADMIN",
                        "ROLE_MYPAYMENTS_COMPANY_ADMIN", "ROLE_TASK_COMPANY_ADMIN", "ROLE_HELPDESK_COMPANY_ADMIN",
                        "ROLE_ACCOUNTING_COMPANY_ADMIN", "ROLE_SMARTLEADS_COMPANY_ADMIN", "ROLE_TAXES_COMPANY_ADMIN",
                        "ROLE_MAILER_COMPANY_ADMIN", "ROLE_AI_COMPANY_ADMIN", (RegistrationType) params.get("registrationType"));
            } else {
              createEmployee(employee, roleGroup, (String) params.get("source"), new HashMap<>(),
                        "ROLE_VACATION_COMPANY_ADMIN", "ROLE_PAYROLL_COMPANY_ADMIN", "ROLE_EXPENSE_COMPANY_ADMIN",
                        "ROLE_INVOICE_COMPANY_ADMIN", "ROLE_RECRUIT_USER", "ROLE_LIVECHAT_COMPANY_ADMIN",
                        "ROLE_CRM_COMPANY_ADMIN", "ROLE_MYSHOP_COMPANY_ADMIN", "ROLE_ESIGNATURE_COMPANY_ADMIN",
                        "ROLE_MYPAYMENTS_COMPANY_ADMIN", "ROLE_TASK_COMPANY_ADMIN", "ROLE_HELPDESK_COMPANY_ADMIN",
                        "ROLE_ACCOUNTING_COMPANY_ADMIN", "ROLE_SMARTLEADS_COMPANY_ADMIN", "ROLE_TAXES_COMPANY_ADMIN",
                        "ROLE_MAILER_COMPANY_ADMIN", "ROLE_AI_COMPANY_ADMIN", (RegistrationType) params.get("registrationType"));
            }
            User finalEmployee = employee;
            apiService.executeAfterCommit(() -> updateBillingCompany(finalEmployee.getUniqueId()));
        } else {
            // Handle the case when an employee with the same email already exists
            return "Email id is already used.";
        }

        return email;
    }

    public void updateBillingCompany(String uniqueId) {
        try {
            String url = "UPDATE_ADMIN_UID";
            restTemplate.postForObject(url,Collections.singletonMap("uid", uniqueId), String.class);
        } catch (Exception e) {
            logger.error((Marker) Level.SEVERE,"Can not update uniqueId to https://commons.getzyk.com/billing/register/company", e);
        }
    }


    public User createEmployee(User employee, String peopleRoleGroup, String source, Map<String, Object> emailContent, String vacation_role, String payroll_role, String expense_role, String invoice_role , String recruit_role, String livechat_role , String crm_role , String myshop_role , String esignature_role , String mypayments_role, String task_role, String helpdesk_role, String accounting_role, String smartleads_role, String taxes_role, String mailer_role , String ai_role, RegistrationType registrationType) {


        employee.setAcceptTOS(true);
        employee.setSource(registrationType);

        if (registrationType.equals(RegistrationType.Mobile)) {
            employee.setMobile(employee.getEmail());
        }

//        employee.setPassword(employee.getPassword() != null ? employee.getPassword() : StrUtil.randomPassword();
//        String tempPassword = employee.getPassword();

        employee = saveEmployeeInfo(employee);

        RoleGroup peopleRole = roleGroupRepository.findByName(peopleRoleGroup);
        Role vacationRoleObj = roleRepository.findByAuthority(vacation_role);
        Role payrollRoleObj = roleRepository.findByAuthority(payroll_role);
        Role expenseRoleObj = roleRepository.findByAuthority(expense_role);
        Role invoiceRoleObj = roleRepository.findByAuthority(invoice_role);
        Role recruitRoleObj = roleRepository.findByAuthority(recruit_role);
        Role livechatRoleObj = roleRepository.findByAuthority(livechat_role);
        Role crmRoleObj = roleRepository.findByAuthority(crm_role);
        Role myshopRoleObj = roleRepository.findByAuthority(myshop_role);
        Role esignatureRoleObj = roleRepository.findByAuthority(esignature_role);
        Role mypaymentsRoleObj = roleRepository.findByAuthority(mypayments_role);
        Role taskRoleObj = roleRepository.findByAuthority(task_role);
        Role helpdeskRoleObj = roleRepository.findByAuthority(helpdesk_role);
        Role accountingRoleObj = roleRepository.findByAuthority(accounting_role);
        Role smartleadsRoleObj = roleRepository.findByAuthority(smartleads_role);
        Role taxesRoleObj = roleRepository.findByAuthority(taxes_role);
        Role mailerRoleObj = roleRepository.findByAuthority(mailer_role);
        Role aiRoleObj = roleRepository.findByAuthority(ai_role);



        saveEmployeeRoleGroup(employee, peopleRole, vacationRoleObj, payrollRoleObj,expenseRoleObj,invoiceRoleObj,recruitRoleObj,livechatRoleObj,crmRoleObj,myshopRoleObj,esignatureRoleObj,mypaymentsRoleObj,taskRoleObj,helpdeskRoleObj,accountingRoleObj,smartleadsRoleObj,taxesRoleObj,mailerRoleObj,aiRoleObj);

//        if (registrationType.equals(RegistrationType.EMAIL)) {
//            sendMailService.sendUserVerificationEmail(employee, tempPassword, source, emailContent);
//            gcmService.sendVerificationNotification(employee);
//        } else if (registrationType.equals(RegistrationType.MOBILE)) {
//            sendAndSaveOtp(employee.getMobile(), employee.getUniqueId());
//        }

        return employee;
    }

    public User saveEmployeeInfo(User employeeInstance) {
        try {
            userService.save(employeeInstance);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save employee information", e);
        }

        return employeeInstance;
    }

    public void saveEmployeeRoleGroup(User employee, RoleGroup peopleRoleGroup, Role vacation, Role payroll, Role imprest, Role invoice, Role hire, Role livechat, Role crm, Role myshop, Role esignature, Role mypayments, Role task, Role helpdesk, Role accounting, Role smartleads, Role taxes, Role mailer, Role ai) {
        try {
            User employeeRole = EmployeeRole.findByEmployee(employee);
            Long loanRole;
            Long contractorRole;
            Company company = employee.getCompany();
            if (company != null && companyRepository.getIsIndividual()) {
                loanRole = Role.getExternalLoanEmployee();
                contractorRole = Role.getExternalContractorEmployee();
            } else {
                loanRole = Role.getLoanEmployee();
                contractorRole = Role.getContractorEmployee();
            }

            if (employeeRole == null) {
                employeeRole = new User(employee, peopleRoleGroup, imprest, invoice, payroll, hire, vacation, loanRole, contractorRole, livechat, crm, myshop, esignature, mypayments, task, helpdesk, accounting, smartleads, taxes, mailer, ai);
                employeeRole.save(employee);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }



}
