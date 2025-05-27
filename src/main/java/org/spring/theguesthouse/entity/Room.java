package org.spring.theguesthouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Room number is required")
    @Min(value = 1, message = "Room number must be at least 1")
    @Column(name = "room_number", nullable = false, unique = true)
    private Integer roomNumber; // Use Integer for @NotNull validation

    // Max guests with validation and database constraints
    @NotNull(message = "Max guests is required")
    @Min(value = 1, message = "Room must accommodate at least 1 guest")
    @Max(value = 20, message = "Maximum 20 guests allowed per room")
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    // Bidirectional relationship
    @OneToMany(mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
}