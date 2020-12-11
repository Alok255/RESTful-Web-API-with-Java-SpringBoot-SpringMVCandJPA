package com.restful.web.security;

//constant class

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // Expiration time for 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "j@v@spr!^9";
}
