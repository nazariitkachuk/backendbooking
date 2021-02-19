package com.hotelapp.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private RoomType roomType;

    private Long places;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;
}
