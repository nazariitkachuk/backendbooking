package com.hotelapp.daos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
