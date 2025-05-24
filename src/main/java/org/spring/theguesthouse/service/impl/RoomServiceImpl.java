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
    public List<RoomDto> getAllRooms() {
        return roomRepo.findAll().stream().map(this::roomToDto).toList();
    }

    // New, simplified version using the isRoomAvailable method
    @Override
    public List<RoomDto> getAllAvailableRooms(Date startDate, Date endDate) {
        return roomRepo.findAll().stream()
                .filter(room -> isRoomAvailable(room.getId(), startDate, endDate))
                .map(this::roomToDto)
                .toList();
    }

    @Override
    public boolean isRoomAvailable(Long roomId, Date startDate, Date endDate, Long excludeBookingId) {
        // Validate that the room exists
        if (!roomRepo.existsById(roomId)) {
            return false;
        }

        // Check if the room has any conflicting bookings
        return bookingRepo.findAll().stream()
                .filter(booking -> booking.getRoom().getId().equals(roomId))
                .filter(booking -> excludeBookingId == null || !booking.getId().equals(excludeBookingId))
                .noneMatch(booking -> {
                    // Check if the date ranges overlap
                    return !endDate.before(booking.getStartDate()) && !startDate.after(booking.getEndDate());
                });
    }

    // Overloaded method with 3 parameters (simpler version)
    @Override
    public boolean isRoomAvailable(Long roomId, Date startDate, Date endDate) {
        return isRoomAvailable(roomId, startDate, endDate, null);
    }
}
