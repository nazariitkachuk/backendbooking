package com.hotelapp.configuration.security.utils;

import com.hotelapp.configuration.security.daos.CookieAuthResponse;
import com.hotelapp.configuration.security.daos.Token;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class UserUtils {

    public UserUtils(@Value("${token.expirationInDays}") Long accessTokenDurationInDays,
                     CookieUtil cookieUtil) {
        if (accessTokenDurationInDays == null || accessTokenDurationInDays.equals(0)) {
            accessTokenDurationInDays = 1L;
        }
        this.accessTokenDuration = accessTokenDurationInDays * 24 * 3600 * 1000;
        this.cookieUtil = cookieUtil;
    }

    private Long accessTokenDuration;

    @NonNull
    private CookieUtil cookieUtil;

    /**
     *
     * @param headers mutable parameter- token is added to cookies
     * @param token generated token added to Cookie header
     */
    private void addAccessTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    /**
     * deletes cookie with token
     * @param headers mutable parameter
     */
    private void deleteAccessTokenCookie(HttpHeaders headers) {
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
    }

    public ResponseEntity<?> login(String username, String accessToken, List<String> authorities) {
        LocalDateTime expiryDate = LocalDateTime.now().plus(Duration.ofMillis(this.accessTokenDuration));
        Token token = new Token(Token.TokenType.ACCESS, accessToken, this.accessTokenDuration, expiryDate);
        HttpHeaders httpHeaders = new HttpHeaders();
        addAccessTokenCookie(httpHeaders, token);
        return ResponseEntity.accepted().headers(httpHeaders).body(new CookieAuthResponse(username, "en", authorities)); // TODO check body
    }

    public ResponseEntity<?> logout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        deleteAccessTokenCookie(httpHeaders);
        return ResponseEntity.ok().headers(httpHeaders).body("logged out");
    }
}
