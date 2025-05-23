
package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedBookingDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
    private CustomerDto customer;
    private RoomDto room;
}
