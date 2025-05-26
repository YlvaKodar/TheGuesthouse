package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", start: " + startDate +
                ", end: " + endDate +
                '}';
    }
}
