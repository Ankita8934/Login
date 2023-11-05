package com.service.login.auth.co;

import lombok.Data;

@Data
public class SignUpCO {

    String name;
    String email;
    String password;
    String confirmPassword;

}
