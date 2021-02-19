package com.hotelapp.daos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(value = {"id"},allowGetters = true)
@Getter
@Setter
public class SimpleReservationDto {

    private Long id;

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

    private Boolean active=false;

    @NotNull
    private String roomType;
}
