package com.hotelapp.daos;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotelapp.validation.EmailUnique;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {


    @JsonProperty(value = "id",access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(min = 1, max = 60)
    @NotBlank
    private String username;


    private String firstName;

    private String lastName;

    private String phoneNumber;

    @EmailUnique
    @NotNull
    private String email;


    @NotBlank
    @Size(min = 2, max = 50)
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;


}
