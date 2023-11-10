package com.service.login.auth.enums;

public enum RegistrationType {

    mobile("mobile"),
    email("email"),
    google("google");

    private final String name;

    RegistrationType(String name) {
        this.name = name;
    }
}
