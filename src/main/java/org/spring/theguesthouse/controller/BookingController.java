package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path ="/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    //localhost:8080/bookings/all
    @RequestMapping("/all")
    public String showAllCustomers(Model model) {
        List<BookingDTO> bookingList = bookingService.getAllBookingDtos();
        model.addAttribute("bookingTitle", "Bookings");
        model.addAttribute("allBookings", bookingList);
        model.addAttribute("id", "ID");
        model.addAttribute("startDate", "START");
        model.addAttribute("endDate", "END");
        return "showAllBookings";
    }

}
