package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.dto.DetailedBookingDTO;

import java.util.List;

public interface BookingService {
    BookingDTO bookingToDto(Booking booking);
    DetailedBookingDTO bookingToDetailedDto(Booking booking);
    Booking detailedBookingDtoToBooking(DetailedBookingDTO dto);
    String addBooking(DetailedBookingDTO booking);
    List<BookingDTO> getAllBookingDtos();
    List<DetailedBookingDTO> getAllDetailedBookingDtos();
}
