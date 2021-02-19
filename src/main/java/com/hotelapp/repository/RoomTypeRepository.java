package com.hotelapp.repository;

import com.hotelapp.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {


    Optional<RoomType> findByName(String name);

    long deleteByTypeId(Long id);
    //long deleteById(Long id);
}
