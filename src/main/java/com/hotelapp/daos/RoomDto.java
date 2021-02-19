package com.hotelapp.daos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotelapp.entities.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RoomDto {

    private Long roomId;

    private String roomType;

    private Long places;

}
