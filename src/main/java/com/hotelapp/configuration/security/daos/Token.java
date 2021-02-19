package com.hotelapp.configuration.security.daos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Token {

    private TokenType tokenType;
    private String tokenValue;
    private  Long duration;
    private LocalDateTime expiryDate;

    public enum  TokenType{
        ACCESS
    }
}
