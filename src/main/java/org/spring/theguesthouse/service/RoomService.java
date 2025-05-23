package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Room;

import java.util.Date;
import java.util.List;

public interface RoomService {

    public RoomDto roomToDto(Room r);
    public Room roomDtoToRoom(RoomDto r);
    public List<RoomDto> getAllRooms();
    public List<RoomDto> getAllAvailableRooms(Date startDate, Date endDate);
}
