package org.spring.theguesthouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull (message = "Start date is required")
    @FutureOrPresent (message = "Start date can't be in the past")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull (message = "End date is required")
    @FutureOrPresent (message = "End date must be in the future")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull (message = "Number of guests is required")
    @Min(value = 1, message = "At least 1 guest is required")
    @Max(value = 4, message = "Maximum 4 guests per room")
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;

    @NotNull (message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_customer"))
    private Customer customer;

    @NotNull (message = "Room is required")
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_room"))
    private Room room;

    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) return true;
        return endDate.isAfter(startDate);
    }
}
