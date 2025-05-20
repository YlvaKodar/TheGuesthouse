package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;

    @Override
    public RoomDto roomToDto(Room r) {
        return RoomDto.builder().id(r.getId()).roomNumber(r.getRoomNumber()).build();
    }

    @Override
    public Room roomDtoToRoom(RoomDto r) {
        return Room.builder().id(r.getId()).roomNumber(r.getRoomNumber()).build();
    }

    @Override
    public List<RoomDto> getAllRooms() {
        return roomRepo.findAll().stream().map(this::roomToDto).toList();
    }
}
