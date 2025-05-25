package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.impl.CustomerServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(path ="/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final CustomerServiceImpl customerServiceImpl;

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

    @GetMapping("/create/{customerId}")
    public String showCreateBooking(@PathVariable Long customerId, Model model) {
        DetailedCustomerDto customer = customerServiceImpl.getCustomerById(customerId);
        model.addAttribute("booking", new BookingDTO());
        model.addAttribute("customer", customer);
        return "createBooking";
    }

    @PostMapping("/create{customerId}/")
    public String createBooking(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder().startDate(startDate).endDate(endDate).customer(customer).build();
        bookingService.addBooking(booking);
        return "redirect:/all";
    }

}
