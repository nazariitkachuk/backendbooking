package com.hotelapp.services;

import com.hotelapp.daos.RoomDto;
import com.hotelapp.entities.Room;
import com.hotelapp.entities.RoomType;
import com.hotelapp.exceptions.DataDuplicateExceptoion;
import com.hotelapp.exceptions.DataNotFoundException;
import com.hotelapp.repository.RoomTypeRepository;
import com.hotelapp.repository.RoomsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomsService {

    @NonNull
    private  RoomsRepository roomsRepository;

    @NonNull
    private  RoomTypeRepository typeRepository;

    private Function<Room,RoomDto> roomToDto = e->{
            RoomDto dto = new RoomDto();
                    BeanUtils.copyProperties(e,dto);
                    dto.setRoomType(e.getRoomType().getName());
                    return dto;
    };

    private Function<RoomDto,Room> dtoToRoom = e->{
        Room room = new Room();
        BeanUtils.copyProperties(e,room);
        return room;
    };

    @Transactional
    public List<RoomDto> getRoomsList(){
        return roomsRepository.findAll().stream()
                .map(roomToDto).collect(Collectors.toList());
    }
    @Transactional
    public List<RoomDto> addRoom(RoomDto roomDto){
        roomDto.setRoomId(null);
        RoomType type=typeRepository.findByName(roomDto.getRoomType())
                .orElseThrow(()->new DataNotFoundException("Not found roomType : "+roomDto.getRoomType()));
        Room room = dtoToRoom.apply(roomDto);
        room.setRoomType(type);
        roomsRepository.save(room);
       return getRoomsList();
    }
    @Transactional
    public List<RoomDto> removeRoom(Long roomId){
        long result=roomsRepository.deleteByRoomId(roomId);

        return getRoomsList();
    }


    @Transactional
    public List<RoomType> addType(RoomType type ){
        if(typeRepository.findByName(type.getName()).isPresent()){
            throw new DataDuplicateExceptoion("RoomType",type.getName());
        }
        type.setTypeId(null);
        typeRepository.save(type);
        return typeRepository.findAll();
    }

    @Transactional
    public List<RoomType> removeType(Long typeID ){
        long result=typeRepository.deleteByTypeId(typeID);
        return typeRepository.findAll();
    }



}
