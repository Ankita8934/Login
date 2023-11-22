package com.service.login.auth.co;

import org.springframework.http.HttpStatus;

public class ResponseEntity<T> {


    String code = String.valueOf(HttpStatus.OK.value());
    String message;
    T data;

    public ResponseEntity(T data, String message){
        this.data = data;
        this.message = message;
    }
}
