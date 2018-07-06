package com.tests.jm.jwtauthetication.utils;

public class Contants {

    // EXPIRATION_TIME = 10 dias
    public static final long EXPIRATION_TIME = 860_000_000;

    public static final String SECRET = "MySecret";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

}
