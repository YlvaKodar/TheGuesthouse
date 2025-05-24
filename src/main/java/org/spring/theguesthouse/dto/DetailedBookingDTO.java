
package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedBookingDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private CustomerDto customer;
    private RoomDto room;
}
