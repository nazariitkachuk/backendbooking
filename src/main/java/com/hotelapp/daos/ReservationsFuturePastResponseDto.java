package com.hotelapp.daos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class ReservationsFuturePastResponseDto {
    private final List<ReservationDto> future;
    private final List<ReservationDto> past;
}
