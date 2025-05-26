package org.spring.theguesthouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {
    private Long id;
    private int roomNumber;
    private int maxGuests;

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", nr: " + roomNumber +
                ", beds available: " + maxGuests +
                '}';
    }
}
