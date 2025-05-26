package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.*;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.CustomerService;
import org.spring.theguesthouse.service.RoomService;
import org.spring.theguesthouse.service.impl.BookingServiceImpl;
import org.spring.theguesthouse.service.impl.CustomerServiceImpl;
import org.spring.theguesthouse.service.impl.RoomServiceImpl;
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
    private final CustomerService customerService;
    private final RoomService roomService;


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
    //localhost:8080/bookings/create
    @GetMapping("/create/{customerId}")
    public String showCreateBooking(@PathVariable Long customerId, Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        model.addAttribute("booking", new BookingDTO());
        model.addAttribute("customer", customer);
        return "createBooking";
    }

    @PostMapping("/create/{customerId}/room-availability")
    public String showRoomAvailability(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate);

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customer);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "createBooking";
    }

    @PostMapping("/create/{customerId}")
    public String createBooking(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam Long roomId) {
        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        RoomDto room = RoomDto.builder().id(roomId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder().startDate(startDate).endDate(endDate).customer(customer).room(room).build();
        bookingService.addBooking(booking);
        return "redirect:/bookings/all";
    }

}
