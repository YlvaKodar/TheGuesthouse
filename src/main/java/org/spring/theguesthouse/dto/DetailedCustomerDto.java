package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DetailedCustomerDto {

    private Long id;
    private String name;
    private String email;
    private List<BookingDTO> bookings;
}
