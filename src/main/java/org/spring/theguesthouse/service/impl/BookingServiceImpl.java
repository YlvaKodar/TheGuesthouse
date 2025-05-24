package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final CustomerRepo customerRepo;
    private final RoomRepo roomRepo;

    @Override
    public BookingDTO bookingToDto(Booking booking) {
        return BookingDTO.builder().id(booking.getId()).startDate(booking.getStartDate()).endDate(booking.getEndDate()).build();
    }

    @Override
    public DetailedBookingDTO bookingToDetailedDto(Booking booking) {
        return DetailedBookingDTO.builder().id(booking.getId())
                .startDate(booking.getStartDate()).endDate(booking.getEndDate())
                .customer(new CustomerDto(booking.getCustomer().getId(), booking.getCustomer().getName()))
                .room(new RoomDto(booking.getRoom().getId(), booking.getRoom().getRoomNumber()))
                .build();
    }

    @Override
    public Booking detailedBookingDtoToBooking(DetailedBookingDTO dto) {
        // Return null if the DTO is null
        if (dto == null) {
            return null;
        }

        //Get customer or throw exception
        Customer customer = customerRepo.findById(dto.getCustomer().getId()).
                orElseThrow(() -> new RuntimeException("Cannot make booking; customer reference is missing"));

        //Get room or throw exception
        Room room = roomRepo.findById(dto.getRoom().getId()).
                orElseThrow(() -> new RuntimeException("Cannot make booking; room reference is missing"));

        //Create new Booking using builder pattern
        return Booking.builder()
                .id(dto.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .customer(customer)
                .room(room)
                .build();
    }

    @Override
    public DetailedBookingDTO getBookingById(long id) {
        return bookingRepo.findById(id).map(this::bookingToDetailedDto).orElse(null);
    }

    @Override
    public String addBooking(DetailedBookingDTO booking) {
        bookingRepo.save(detailedBookingDtoToBooking(booking));
        return "Booking successfully added";
    }

    @Override
    public DetailedBookingDTO updateBooking(DetailedBookingDTO booking) {
        // Validate input
        if (booking.getId() == null) {
            throw new RuntimeException("Booking ID is required for update");
        }

        // Validate dates
        if (booking.getStartDate().after(booking.getEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }

        // Find the existing booking or throw exception if not found
        Booking existingBooking = bookingRepo.findById(booking.getId())
                .orElseThrow(() -> new RuntimeException("Booking with id " + booking.getId() + " not found"));

        // Determine which room to check availability for
        Long roomIdToCheck = (booking.getRoom() != null) ?
                booking.getRoom().getId() : existingBooking.getRoom().getId();

        // Check room availability using RoomService (excluding current booking)
        if (!roomService.isRoomAvailable(roomIdToCheck, booking.getStartDate(),
                booking.getEndDate(), booking.getId())) {
            Room room = roomRepo.findById(roomIdToCheck)
                    .orElseThrow(() -> new RuntimeException("Room with id " + roomIdToCheck + " not found"));
            throw new RuntimeException("Room " + room.getRoomNumber() +
                    " is not available for the selected dates");
        }

        // Update dates (always allowed)
        existingBooking.setStartDate(booking.getStartDate());
        existingBooking.setEndDate(booking.getEndDate());

        // Update room only if provided and different
        if (booking.getRoom() != null &&
                !existingBooking.getRoom().getId().equals(booking.getRoom().getId())) {
            Room newRoom = roomRepo.findById(booking.getRoom().getId())
                    .orElseThrow(() -> new RuntimeException("Room with id " + booking.getRoom().getId() + " not found"));
            existingBooking.setRoom(newRoom);
        }

        // Customer remains the same - no customer updates allowed

        // Save the updated booking
        Booking updatedBooking = bookingRepo.save(existingBooking);

        // Convert back to DTO and return
        return bookingToDetailedDto(updatedBooking);
    }
    @Override
    public List<BookingDTO> getAllBookingDtos() {
        return bookingRepo.findAll().stream().map(this::bookingToDto).toList();
    }

    @Override
    public List<DetailedBookingDTO> getAllDetailedBookingDtos() {
        return bookingRepo.findAll().stream().map(this::bookingToDetailedDto).toList();
    }
}
