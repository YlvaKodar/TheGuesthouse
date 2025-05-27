package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        return RoomDto.builder().id(r.getId()).roomNumber(r.getRoomNumber()).maxGuests(r.getMaxGuests()).build();
    }

    @Override
    public List<RoomDto> getAllRooms() {
        return roomRepo.findAll().stream().map(this::roomToDto).toList();
    }

    // New, simplified version using the isRoomAvailable method
    @Override
    public List<RoomDto> getAllAvailableRooms(LocalDate startDate, LocalDate endDate, int numberOfGuests) {

        List<RoomDto> allAvailableRooms = roomRepo.findAll().stream()
                .filter(room -> room.getMaxGuests() >= numberOfGuests) // Capacity check
                .filter(room -> isRoomAvailable(room.getId(), startDate, endDate)) // Availability check
                .map(this::roomToDto)
                .toList();

        List<RoomDto> sizePrio = new ArrayList<>();
        //Om möjligt, gör lista med bara rum som passar, i andra hand med lite för stora osv ...
        for (int targetSize = numberOfGuests; targetSize <= 4; targetSize++) {
            int finalTargetSize = targetSize;
            sizePrio = allAvailableRooms.stream()
                    .filter(r -> r.getMaxGuests() == finalTargetSize)
                    .toList();

            if (!sizePrio.isEmpty()) {
                break;
            }
        }
        //Lista med i första hand rätt antal, i andra hand större.
        return sizePrio;
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate, Long excludeBookingId) {
        // Validate that the room exists
        //Booking existingBooking = bookingRepo.findById(booking.getId()).orElseThrow(() -> new RuntimeException("Booking with id" + booking.getId() + " not found"));

        // Check if the room has any conflicting bookings
        return bookingRepo.findAll().stream()
                .filter(booking -> booking.getRoom().getId().equals(roomId))
                .filter(booking -> !booking.getId().equals(excludeBookingId))
                .noneMatch(booking -> {
                    // Check if the date ranges overlap
                    return !endDate.isBefore(booking.getStartDate()) && !startDate.isAfter(booking.getEndDate());
                });
    }

    // Overloaded method with 3 parameters (simpler version)
    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate startDate, LocalDate endDate) {
        return isRoomAvailable(roomId, startDate, endDate, null);
    }
}
