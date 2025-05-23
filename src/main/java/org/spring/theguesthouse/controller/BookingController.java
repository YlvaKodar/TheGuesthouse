package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.repository.BookingRepo;
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
    private final BookingRepo bookingRepo; //TODO radera när bookingService är utbyggd

    //localhost:8080/bookings/all
    @RequestMapping("/all")
    public String showAllBookings(Model model) {
        List<BookingDTO> bookingDtoList = bookingService.getAllBookingDtos();
        model.addAttribute("bookingTitle", "All Bookings");
        model.addAttribute("allBookings", bookingDtoList);
        model.addAttribute("id", "ID");
        model.addAttribute("start", "Start Date");
        model.addAttribute("end", "End Date");
        return "showAllBookings";
    }

    //TODO Gå via Service
    @RequestMapping(path = "/deleteById/{id}")
    public String deleteBookingById(@PathVariable Long id, Model model) {
        bookingRepo.deleteById(id);
        return "redirect:/bookings/all";
    }

}
