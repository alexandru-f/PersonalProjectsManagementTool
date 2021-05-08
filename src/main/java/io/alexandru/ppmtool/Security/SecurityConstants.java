package io.alexandru.ppmtool.Security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecurityConstants {

    public static final String LOGIN_SIGN_UP = "/api/users/login";
    public static final String REGISTER = "/api/users/register";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET = "SecretKeyToGenerateJwTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 3000000;

    public static final String[] EXCLUDED_LOGIN_URLS = new String[] {"/api/users/login",
            "/api/users/register", "/api/users/forgotPassword", "/api/users/resetPassword"};
}
