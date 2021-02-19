package com.hotelapp.daos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotelapp.validation.EmailSyntax;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(value = {"id"},allowGetters = true)
public class ReservationDto {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @EmailSyntax
    @NotNull
    private String email;

    private LocalDateTime bookingTime;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate validFrom;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate validTo;

    @NotNull
    private Long adultsNumber;

    private Long childrenNumber;

    private String questions;

    private Boolean isUserAssigned=false;

    private Boolean active=false;

    @NotNull
    private String roomType;
}
