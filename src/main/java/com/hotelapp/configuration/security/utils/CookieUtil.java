package com.hotelapp.configuration.security.utils;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


/**
 * Cookie util to add and remove jwt token from response
 */
@Component
@Validated
public class CookieUtil {

    @Value("${authentication-test.auth.accessTokenCookieName}")
    @NotNull
    private String accessCookieName;



    public HttpCookie createAccessTokenCookie(String token, Long duration){

        return ResponseCookie.from(accessCookieName,token)
                .maxAge(duration)
                .httpOnly(true)
                .path("/")
                .build();

    }

    public HttpCookie deleteAccessTokenCookie(){
        return ResponseCookie.from(accessCookieName,"").maxAge(0).path("/").build();
    }
}
