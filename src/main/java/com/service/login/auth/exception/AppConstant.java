package com.service.login.auth.exception;

import com.service.login.auth.domain.FreeEmailProvider;

import java.util.List;

public class AppConstant {
    public static final List<String> restrictedDomains = FreeEmailProvider.fetchAllRestrictedDomains();
    public static final String ADMIN_EMAIL = "theforce@getzyk.com";
}
