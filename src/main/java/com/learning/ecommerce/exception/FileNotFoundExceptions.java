package com.learning.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileNotFoundExceptions extends RuntimeException{
    
    public FileNotFoundExceptions (String msg) {
        super(msg);
    }

    public FileNotFoundExceptions (String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
