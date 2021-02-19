package com.hotelapp.daos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class ReservationResponseDto {
    private final List<ReservationDto> active;
    private final List<ReservationDto> inactive;
}
