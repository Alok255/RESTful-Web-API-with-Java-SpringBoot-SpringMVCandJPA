package com.restful.web.exceptions;

public class UserServiceExceptions extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserServiceExceptions(String message) {
        super(message);
    }
}
