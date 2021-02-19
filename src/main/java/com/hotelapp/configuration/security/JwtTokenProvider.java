package com.hotelapp.configuration.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenProvider {

    private Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private String jwtSecret;
    private int jwtExpirationInDays;
    private int jwtExpirationInMs;

    public JwtTokenProvider(
            @Value("${token.expirationInDays}") int jwtExpirationInDays,
            @Value("${token.jwtSecret}") String jwtSecret) {

        this.jwtSecret = jwtSecret;
        this.jwtExpirationInDays = jwtExpirationInDays;
        this.jwtExpirationInMs = jwtExpirationInDays * 24 * 3600 * 1000;


    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()

                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()), SignatureAlgorithm.HS512) //SIGN algorithm
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()))
                .parseClaimsJws(token)
                .getBody();


        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(this.jwtSecret.getBytes())).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}