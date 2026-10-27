package com.spring.gateway.management.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private final HttpStatus status;

    public ApiException(HttpStatus status,String mssg){
        super(mssg);
        this.status=status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
