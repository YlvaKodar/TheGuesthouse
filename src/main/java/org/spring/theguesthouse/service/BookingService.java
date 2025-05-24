package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.DeleteResponseDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.dto.DetailedBookingDTO;

import java.util.List;

public interface BookingService {
    BookingDTO bookingToDto(Booking booking);
    DetailedBookingDTO bookingToDetailedDto(Booking booking);
    Booking detailedBookingDtoToBooking(DetailedBookingDTO dto);
    DetailedBookingDTO getBookingById(long id);
    String addBooking(DetailedBookingDTO booking);
    DetailedBookingDTO updateBooking(DetailedBookingDTO booking);
    List<BookingDTO> getAllBookingDtos();
    List<DetailedBookingDTO> getAllDetailedBookingDtos();
    DeleteResponseDto deleteBooking(Long id);
}
