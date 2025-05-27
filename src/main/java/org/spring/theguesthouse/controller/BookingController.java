package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.*;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.CustomerService;
import org.spring.theguesthouse.service.RoomService;
import org.spring.theguesthouse.service.impl.BookingServiceImpl;
import org.spring.theguesthouse.service.impl.CustomerServiceImpl;
import org.spring.theguesthouse.service.impl.RoomServiceImpl;
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
                                @RequestParam int numberOfGuests,
                                @RequestParam Long roomId) {

        RoomDto room = RoomDto.builder().id(roomId).build();

        DetailedBookingDTO updatedBooking = DetailedBookingDTO.builder()
                .id(id)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .numberOfGuests(numberOfGuests)
                .room(room)
                .build();

        try {
            bookingService.updateBooking(updatedBooking);
            return "redirect:/bookings/details/" + id;
        } catch (RuntimeException e) {
            return "redirect:/bookings/details/" + id + "?error=" + e.getMessage();
        }
    }

    @RequestMapping(path = "/deleteById/{id}")
    public String deleteBookingById(@PathVariable Long id, Model model) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings/all";
    }

    @GetMapping("/create/{customerId}")
    public String showCreateBooking(@PathVariable Long customerId, Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        model.addAttribute("booking", new BookingDTO());
        model.addAttribute("customer", customer);
        return "createBooking";
    }

    @PostMapping("/create/{customerId}/room-availability")
    public String showRoomAvailability(@PathVariable Long customerId,
                                       @RequestParam LocalDate startDate,
                                       @RequestParam LocalDate endDate,
                                       @RequestParam int numberOfGuests,
                                       Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customer);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);
        return "createBooking";
    }

    @PostMapping("/create/{customerId}")
    public String createBooking(@PathVariable Long customerId,
                                @RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate,
                                @RequestParam int numberOfGuests,
                                @RequestParam Long roomId,
                                Model model) {

        if (numberOfGuests < 1 || numberOfGuests > 4) {
            model.addAttribute("error", "Number of guests must be between 1 and 4");
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }

        if (!roomService.canRoomAccommodateGuests(roomId, numberOfGuests)) {
            model.addAttribute("error", "Selected room can't accomodate " + numberOfGuests + " guests");
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }

        if (!roomService.isRoomAvailable(roomId, startDate, endDate)) {
            model.addAttribute("error", "Selected room is not available for the chosen dates");
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }

        if (startDate.isBefore(LocalDate.now())) {
            model.addAttribute("error", "Start date can't be in the past");
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }

        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            model.addAttribute("error", "Start date must be before end date");
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }

        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        RoomDto room = RoomDto.builder().id(roomId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .customer(customer)
                .room(room)
                .build();

        try {
            bookingService.addBooking(booking);
            return "redirect:/bookings/all/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return repopulateCreateBookingForm(customerId, startDate, endDate, numberOfGuests, model);
        }
    }

    private String repopulateCreateBookingForm(Long customerId, LocalDate startDate, LocalDate endDate, int numberOfGuests, Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customer);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);
        return "createBooking";
    }

}
