package com.hotelapp.daos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class DeleteDto {

    private final Integer deletedCount;
}
