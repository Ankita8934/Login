package com.service.login.auth.co;

import lombok.Data;

@Data
public class SignUpCO {

    String userName;
    String email;
    String password;
    String confirmPassword;

}
