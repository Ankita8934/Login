package com.service.login.auth.enums;

public enum CompanyType {
    Regular("Regular"),
    Individual("Individual");

    private final String name;

    CompanyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CompanyType[] list() {
        return CompanyType.values();
    }
}
