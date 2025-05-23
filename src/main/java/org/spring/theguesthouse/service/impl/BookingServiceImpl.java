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
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final RoomService roomService;
    private final BookingRepo bookingRepo;
    private final CustomerRepo customerRepo;

    @Override
    public BookingDTO bookingToDto(Booking booking) {
        return BookingDTO.builder().id(booking.getId()).startDate(booking.getStartDate()).endDate(booking.getEndDate()).build();
    }

    @Override
    public DetailedBookingDTO bookingToDetailedDto(Booking booking) {
        return DetailedBookingDTO.builder().id(booking.getId())
                .startDate(booking.getStartDate()).endDate(booking.getEndDate())
                .customer(new CustomerDto(booking.getCustomer().getId(), booking.getCustomer().getName()))
                .rooms(booking.getRooms().stream().map(roomService::roomToDto).toList())
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

        List<Room> rooms = dto.getRooms().stream().map(roomService::roomDtoToRoom).toList();

        //Create new Booking using builder pattern
        return Booking.builder()
                .id(dto.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .customer(customer)
                .rooms(rooms)
                .build();
    }

    @Override
    public String addBooking(DetailedBookingDTO booking) {
        bookingRepo.save(detailedBookingDtoToBooking(booking));
        return "Booking successfully added";
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
