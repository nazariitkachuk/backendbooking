package com.hotelapp.repository;

import com.hotelapp.entities.Room;
import com.hotelapp.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomsRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomType(RoomType type);

    long deleteByRoomId(Long  roomId);


    @Query(value = "WITH choosen_res as \n" +
            " (SELECT room_id from reservations where " +
            " (:startDate >= valid_from and  :startDate < valid_to) \n" +
            " OR \n" +
            " (:endDate > valid_from and  :endDate < valid_to)) \n" +
            " SELECT s.room_id, s.places  from room s \n" +
            " WHERE s.room_id not in (select room_id from choosen_res)\n" +
            " AND \n" +
            " s.places >= :places \n" +
            " AND \n" +
            " type_id = :roomType \n" +
            " ORDER BY s.places ASC", nativeQuery = true)
    List<Room> findAvailableRooms(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                  @Param("roomType") Long roomType, @Param("places") Long places);
}
