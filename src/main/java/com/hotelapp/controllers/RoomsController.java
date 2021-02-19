package com.hotelapp.controllers;

import com.hotelapp.daos.RoomDto;
import com.hotelapp.entities.RoomType;
import com.hotelapp.services.RoomsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_ADMIN;
import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_HOTEL;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController("rooms")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(allowCredentials="true",allowedHeaders = "*",methods ={GET,POST,PUT,DELETE} ,origins = "*")
@RolesAllowed({ROLE_HOTEL,ROLE_ADMIN})
public class RoomsController {

    @NonNull
    private RoomsService roomsService;

    @PostMapping("add")
    public List<RoomDto> addRoom(@RequestBody @Valid RoomDto roomDto){
        return roomsService.addRoom(roomDto);
    }

    @DeleteMapping("remove")
    public List<RoomDto> deleteRoom(@RequestParam Long roomId){
        return roomsService.removeRoom(roomId);
    }

    @PostMapping("type/add")
    public List<RoomType> adddType(@RequestBody @Valid RoomType roomType){
        return roomsService.addType(roomType);
    }

    @PostMapping("type/delete")
    public List<RoomType> deleteType(@RequestParam  Long typeId){
        return roomsService.removeType(typeId);
    }

    @GetMapping("list")
    public List<RoomDto> getRooms(){
        return roomsService.getRoomsList();
    }
}

