package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;
    private final BookingRepo bookingRepo;

    @Override
    public RoomDto roomToDto(Room r) {
        return RoomDto.builder().id(r.getId()).roomNumber(r.getRoomNumber()).build();
    }

    @Override
    public Room roomDtoToRoom(RoomDto r){
        return Room.builder().roomNumber(r.getRoomNumber()).build();
    }

    @Override
    public List<RoomDto> getAllRooms() {
        return roomRepo.findAll().stream().map(this::roomToDto).toList();
    }

    @Override
    public List<RoomDto> getAllAvailableRooms(Date startDate, Date endDate) {
        
        // Find all rooms that are currently booked
        Set<Long> bookedRoomIds = bookingRepo.findAll().stream()
                .filter(booking -> {
                    // Check if current dates are within the booking period
                    return !endDate.before(booking.getStartDate()) && !startDate.after(booking.getEndDate());
                })
                .flatMap(booking -> booking.getRooms().stream())
                .map(Room::getId)
                .collect(Collectors.toSet());

        // Return all rooms that are NOT currently booked
        return roomRepo.findAll().stream()
                .filter(room -> !bookedRoomIds.contains(room.getId()))
                .map(this::roomToDto)
                .toList();
    }
}
