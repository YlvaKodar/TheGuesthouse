package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CustomerDto {
    private long id;
    private String name;

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", name: " + name + '\n' +
                '}';
    }
}
