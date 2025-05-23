package org.spring.theguesthouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

}
