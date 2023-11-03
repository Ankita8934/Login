package com.service.login.auth.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResponse implements Serializable {

    public static final long serialVersionUID = -906161419554923055L;

    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
