package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DetailedCustomerDto {

    private Long id;
    private String name;
    private String tel;

}
