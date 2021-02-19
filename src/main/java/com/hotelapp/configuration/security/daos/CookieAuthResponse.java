package com.hotelapp.configuration.security.daos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CookieAuthResponse {

    private String username;
    private String language;
    private List<String> roles;
}
