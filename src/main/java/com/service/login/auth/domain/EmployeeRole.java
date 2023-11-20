package com.service.login.auth.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class EmployeeRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private static final long serialVersionUID = 1L;

    @OneToOne
     User employee;
    @OneToOne
    RoleGroup roleGroup;
    private Long vacation_role = Role.getVacationEmployee();
    private Long expense_role = Role.getExpenseEmployee();
    private Long payroll_role = Role.getPayrollEmployee();
    private Long invoice_role = Role.getInvoiceEmployee();
    private Long recruit_role = Role.getRecruitEmployee();
    private Long loan_role = Role.getLoanEmployee();
    private Long contractor_role = Role.getContractorEmployee();
    private Long livechat_role = Role.getLivechatEmployee();
    private Long crm_role = Role.getCrmEmployee();
    private Long myshop_role = Role.getMyshopEmployee();
    private Long esignature_role = Role.getEsignatureEmployee();
    private Long mypayments_role = Role.getMypaymentsEmployee();
    private Long task_role = Role.getTaskEmployee();
    private Long helpdesk_role = Role.getHelpdeskEmployee();
    private Long accounting_role = Role.getAccountingEmployee();
    private Long smartleads_role = Role.getSmartleadsEmployee();
    private Long taxes_role = Role.getTaxesEmployee();
    private Long mailer_role = Role.getMailerEmployee();
    private Long ai_role = Role.getAiEmployee();


    public EmployeeRole(User employee, RoleGroup peopleRoleGroup, Role imprest, Role invoice, Role payroll, Role hire, Role vacation, Long loanRole, Long contractorRole, Role livechat, Role crm, Role myshop, Role esignature, Role mypayments, Role task, Role helpdesk, Role accounting, Role smartleads, Role taxes, Role mailer, Role ai) {
    }
}
