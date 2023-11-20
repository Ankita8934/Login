package com.service.login.auth.domain;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
public class Role implements Serializable {



    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    public String getAuthority() {
        return authority;
    }

    @Column(nullable = false, unique = true)
    private String authority;
    private String module;
    private String description;
    private String domainLevel;
    private String actionLevel;
    private Integer accessLevel;

    private static final Long VACATION_ADMIN = 6L;
    private static final Long VACATION_COMPANY_ADMIN = 7L;
    private static final Long VACATION_BRANCH_ADMIN = 8L;
    private static final Long VACATION_EMPLOYEE = 9L;

    private static final Long EXPENSE_ADMIN = 10L;
    private static final Long EXPENSE_COMPANY_ADMIN = 11L;
    private static final Long EXPENSE_BRANCH_ADMIN = 12L;
    private static final Long EXPENSE_EMPLOYEE = 13L;

    private static final Long PAYROLL_ADMIN = 14L;
    private static final Long PAYROLL_COMPANY_ADMIN = 15L;



    private static final Long PAYROLL_BRANCH_ADMIN = 16L;
    private static final Long PAYROLL_EMPLOYEE = 17L;


    private static final Long INVOICE_ADMIN = 18L;
    private static final Long INVOICE_COMPANY_ADMIN = 19L;
    private static final Long INVOICE_BRANCH_ADMIN = 20L;
    private static final Long INVOICE_EMPLOYEE = 21L;

    private static final Long RECRUIT_ADMIN = 22L;
    private static final Long RECRUIT_COMPANY_ADMIN = 23L;
    private static final Long RECRUIT_BRANCH_ADMIN = 24L;
    private static final Long RECRUIT_EMPLOYEE = 25L;

    private static final Long LOAN_ADMIN = 285L;
    private static final Long LOAN_COMPANY_ADMIN = 286L;
    private static final Long LOAN_BRANCH_ADMIN = 287L;
    private static final Long LOAN_EMPLOYEE = 288L;

    private static final Long CONTRACTOR_ADMIN = 289L;
    private static final Long CONTRACTOR_COMPANY_ADMIN = 290L;
    private static final Long CONTRACTOR_BRANCH_ADMIN = 291L;
    private static final Long CONTRACTOR_EMPLOYEE = 292L;

    private static final Long LIVECHAT_ADMIN = 301L;
    private static final Long LIVECHAT_COMPANY_ADMIN = 302L;
    private static final Long LIVECHAT_BRANCH_ADMIN = 303L;
    private static final Long LIVECHAT_EMPLOYEE = 304L;

    private static final Long CRM_ADMIN = 306L;
    private static final Long CRM_COMPANY_ADMIN = 307L;
    private static final Long CRM_BRANCH_ADMIN = 308L;
    private static final Long CRM_EMPLOYEE = 309L;

    private static final Long MYSHOP_ADMIN = 3011L;
    private static final Long MYSHOP_COMPANY_ADMIN = 312L;
    private static final Long MYSHOP_BRANCH_ADMIN = 313L;
    private static final Long MYSHOP_EMPLOYEE = 314L;

    private static final Long ESIGNATURE_ADMIN = 316L;
    private static final Long ESIGNATURE_COMPANY_ADMIN = 317L;
    private static final Long ESIGNATURE_BRANCH_ADMIN = 318L;


    private static final Long ESIGNATURE_EMPLOYEE = 319L;

    private static final Long MYPAYMENTS_ADMIN = 325L;
    private static final Long MYPAYMENTS_COMPANY_ADMIN = 326L;
    private static final Long MYPAYMENTS_BRANCH_ADMIN = 327L;
    private static final Long MYPAYMENTS_EMPLOYEE = 328L;

    private static final Long TASK_ADMIN = 330L;
    private static final Long TASK_COMPANY_ADMIN = 331L;
    private static final Long TASK_BRANCH_ADMIN = 332L;
    private static final Long TASK_EMPLOYEE = 333L;

    private static final Long HELPDESK_ADMIN = 335L;
    private static final Long HELPDESK_COMPANY_ADMIN = 336L;
    private static final Long HELPDESK_BRANCH_ADMIN = 337L;
    private static final Long HELPDESK_EMPLOYEE = 338L;

    private static final Long EXTERNAL_RECRUIT_EMPLOYEE = 293L;
    private static final Long EXTERNAL_LOAN_EMPLOYEE = 294L;
    private static final Long EXTERNAL_CONTRACTOR_EMPLOYEE = 295L;
    private static final Long EXTERNAL_VACATION_EMPLOYEE = 296L;
    private static final Long EXTERNAL_EXPENSE_EMPLOYEE = 297L;
    private static final Long EXTERNAL_PAYROLL_EMPLOYEE = 298L;
    private static final Long EXTERNAL_INVOICE_EMPLOYEE = 299L;
    private static final Long EXTERNAL_LIVECHAT_EMPLOYEE = 305L;
    private static final Long EXTERNAL_CRM_EMPLOYEE = 310L;
    private static final Long EXTERNAL_MYSHOP_EMPLOYEE = 315L;
    private static final Long EXTERNAL_ESIGNATURE_EMPLOYEE = 320L;
    private static final Long EXTERNAL_MYPAYMENTS_EMPLOYEE = 329L;
    private static final Long EXTERNAL_TASK_EMPLOYEE = 334L;
    private static final Long EXTERNAL_HELPDESK_EMPLOYEE = 339L;
    private static final Long EXTERNAL_ACCOUNTING_EMPLOYEE = 344L;
    private static final Long EXTERNAL_SMARTLEADS_EMPLOYEE = 349L;
    private static final Long EXTERNAL_TAXES_EMPLOYEE = 354L;

    private static final Long ACCOUNTING_ADMIN = 340L;
    private static final Long ACCOUNTING_COMPANY_ADMIN = 341L;
    private static final Long ACCOUNTING_BRANCH_ADMIN = 342L;
    private static final Long ACCOUNTING_EMPLOYEE = 343L;

    private static final Long SMARTLEADS_ADMIN = 345L;
    private static final Long SMARTLEADS_COMPANY_ADMIN = 346L;
    private static final Long SMARTLEADS_BRANCH_ADMIN = 347L;
    private static final Long SMARTLEADS_EMPLOYEE = 348L;

    private static final Long TAXES_ADMIN = 350L;
    private static final Long TAXES_COMPANY_ADMIN = 351L;
    private static final Long TAXES_BRANCH_ADMIN = 352L;
    private static final Long TAXES_EMPLOYEE = 353L;

    private static final Long MAILER_ADMIN = 355L;
    private static final Long MAILER_COMPANY_ADMIN = 356L;
    private static final Long MAILER_BRANCH_ADMIN = 3527L;
    private static final Long MAILER_EMPLOYEE = 358L;
    private static final Long EXTERNAL_MAILER_EMPLOYEE = 359L;

    private static final Long AI_ADMIN = 361L;
    private static final Long AI_COMPANY_ADMIN = 362L;
    private static final Long AI_BRANCH_ADMIN = 363L;
    private static final Long AI_EMPLOYEE = 364L;



    private static final Long EXTERNAL_AI_EMPLOYEE = 365L;


    private static final Long EXTERNAL_VALUE = 0L;

    public static Long getExternalExpenseEmployee() {
        return EXTERNAL_EXPENSE_EMPLOYEE;
    }

    public static Long getExternalRecruitEmployee() {
        return EXTERNAL_RECRUIT_EMPLOYEE;
    }

    public static Long getVacationEmployee() {
        return VACATION_EMPLOYEE;
    }
    public static Long getExpenseEmployee() {
        return EXPENSE_EMPLOYEE;
    }

    public static Long getPayrollEmployee() {
        return PAYROLL_EMPLOYEE;
    }

    public static Long getInvoiceEmployee() {
        return INVOICE_EMPLOYEE;
    }

    public static Long getRecruitEmployee() {
        return RECRUIT_EMPLOYEE;
    }

    public static Long getLoanEmployee() {
        return LOAN_EMPLOYEE;
    }

    public static Long getContractorEmployee() {
        return CONTRACTOR_EMPLOYEE;
    }

    public static Long getLivechatEmployee() {
        return LIVECHAT_EMPLOYEE;
    }

    public static Long getCrmEmployee() {
        return CRM_EMPLOYEE;
    }

    public static Long getMyshopEmployee() {
        return MYSHOP_EMPLOYEE;
    }

    public static Long getEsignatureEmployee() {
        return ESIGNATURE_EMPLOYEE;
    }

    public static Long getMypaymentsEmployee() {
        return MYPAYMENTS_EMPLOYEE;
    }

    public static Long getTaskEmployee() {
        return TASK_EMPLOYEE;
    }

    public static Long getHelpdeskEmployee() {
        return HELPDESK_EMPLOYEE;
    }

    public static Long getAccountingEmployee() {
        return ACCOUNTING_EMPLOYEE;
    }

    public static Long getSmartleadsEmployee() {
        return SMARTLEADS_EMPLOYEE;
    }

    public static Long getTaxesEmployee() {
        return TAXES_EMPLOYEE;
    }

    public static Long getMailerEmployee() {
        return MAILER_EMPLOYEE;
    }

    public static Long getAiEmployee() {
        return AI_EMPLOYEE;
    }

    public static Long getExternalLoanEmployee() {
        return EXTERNAL_LOAN_EMPLOYEE;
    }

    public static Long getExternalContractorEmployee() {
        return EXTERNAL_CONTRACTOR_EMPLOYEE;
    }

    public static Long getExternalVacationEmployee() {
        return EXTERNAL_VACATION_EMPLOYEE;
    }

    public static Long getExternalPayrollEmployee() {
        return EXTERNAL_PAYROLL_EMPLOYEE;
    }

    public static Long getExternalLivechatEmployee() {
        return EXTERNAL_LIVECHAT_EMPLOYEE;
    }

    public static Long getMyshopAdmin() {
        return MYSHOP_ADMIN;
    }
}


