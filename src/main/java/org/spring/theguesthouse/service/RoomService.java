package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Room;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomService {

    public RoomDto roomToDto(Room r);
    public List<RoomDto> getAllRooms();
    public List<RoomDto> getAllAvailableRooms(LocalDate startDate, LocalDate endDate, int numberOfGuests);
    public boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate, Long excludeBookingId);
    public boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate);
    public RoomDto getRoomById(Long roomId);

}
