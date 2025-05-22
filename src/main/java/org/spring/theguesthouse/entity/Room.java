package org.spring.theguesthouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    private int roomNumber;

    @ManyToMany(mappedBy = "rooms")
    private List<Booking> bookings = new ArrayList<>();

}