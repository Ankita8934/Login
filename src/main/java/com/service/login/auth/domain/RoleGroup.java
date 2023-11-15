package com.service.login.auth.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Data
public class RoleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Date dateCreated;
    private Date lastUpdated;
    private int accessLevel = 1;
    private Company company;
    private static final long SUPER_ADMIN = 1L;
    private static final long PEOPLE_ADMIN = 2L;
    private static final long COMPANY_ADMIN = 3L;
    private static final long BRANCH_ADMIN = 4L;
    private static final long EMPLOYEE = 5L;
    public static final long SUPER_ADMIN_ID = 1L;
    public static final long PEOPLE_ADMIN_ID = 2L;


}
