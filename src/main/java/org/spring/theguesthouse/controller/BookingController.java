package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(path ="/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    //localhost:8080/bookings/all
    @RequestMapping("/all")
    public String showAllBookings(Model model) {
        List<BookingDTO> bookingList = bookingService.getAllBookingDtos();
        model.addAttribute("bookingTitle", "Bookings");
        model.addAttribute("allBookings", bookingList);
        model.addAttribute("id", "ID");
        model.addAttribute("startDate", "START");
        model.addAttribute("endDate", "END");
        return "showAllBookings";
    }

    @GetMapping("/details/{id}")
    public String showBookingDetails(@PathVariable Long id, Model model) {
        DetailedBookingDTO booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "detailedBooking";
    }

    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                Model model) {
        DetailedBookingDTO updatedBooking = DetailedBookingDTO.builder()
                .id(id)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();

        bookingService.updateBooking(updatedBooking);
        return "redirect:/bookings/details/" + id;
    }

    @RequestMapping(path = "/deleteById/{id}")
    public String deleteBookingById(@PathVariable Long id, Model model) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings/all";
    }
}
