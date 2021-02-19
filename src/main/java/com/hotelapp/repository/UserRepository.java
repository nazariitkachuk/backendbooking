package com.hotelapp.repository;

import com.hotelapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long>{


    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
