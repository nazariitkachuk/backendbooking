package com.hotelapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    private String name;

    @OneToMany(mappedBy = "roomType",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Room> roomList;

}
