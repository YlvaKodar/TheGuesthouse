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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String tel;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}