package com.service.login.auth.service;


import com.service.login.auth.co.SignUpCO;
import com.service.login.auth.domain.User;
import com.service.login.auth.dto.ResponseDTO;
import com.service.login.auth.exception.InvalidResponseException;

public interface LoginService {

    ResponseDTO<User> sinUp(SignUpCO signUpCO) throws InvalidResponseException;
}
