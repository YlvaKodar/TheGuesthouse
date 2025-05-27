package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.*;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.CustomerService;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.cglib.core.Local;
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
    private final SpringDataWebAutoConfiguration springDataWebAutoConfiguration;


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

        if (booking == null) {
            model.addAttribute("error", "Booking not found.");
            return "errorPage";  // or redirect somewhere appropriate

        }
        model.addAttribute("booking", booking);
        return "detailedBooking";
    }

    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate,
                                @RequestParam Long newRoomId,
                                @RequestParam int numberOfGuests, Model model) {

        // Hämta bokningen
        DetailedBookingDTO booking = bookingService.getBookingById(id);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/customers/all";
        }

        // Hämta den valda rumsinformationen
        RoomDto newRoom = roomService.getRoomById(newRoomId);
        if (newRoom == null) {
            model.addAttribute("error", "Room not found");
            return "redirect:/bookings/details/" + id;
        }

        // Uppdatera bokningen med nya datum och rum
        DetailedBookingDTO updatedBookingDto = DetailedBookingDTO.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .room(newRoom)  // Använd det valda rummet
                .build();

        // Spara den uppdaterade bokningen
        bookingService.updateBooking(updatedBookingDto);

        return "redirect:/bookings/details/" + id; // Ompekning till bokningsdetaljer
    }


    //ÄR DET HÄR VI ÄR NÄR VI KOMMER FRÅN DETAILED BOOKING TILL UPDATE? DÅ VILL VI JU HA IN FLER VÄRDEN,
    //LÄMPLIGTVIS HELA BOOKING-DTO
    /*@PostMapping("/update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate,
                                @RequestParam Long newRoomId,
                                @RequestParam int numberOfGuests, Model model) {

        DetailedCustomerDto customerDto = customerService.getCustomerById(bookingService.getBookingById(id).getCustomer().getId());

        System.out.println("Customers bookingID: " + id);

        if (customerDto == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }

        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "There are no available rooms");
            return "redirect:/customers/all";
        }

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customerDto);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);

        System.out.println("Customers roomID " + newRoomId);

        DetailedBookingDTO updatedBookingDto = DetailedBookingDTO.builder()
            .id(id)
            .startDate(startDate)
            .endDate(endDate)
            .numberOfGuests(numberOfGuests)
            .room(roomService.getRoomById(newRoomId))
            .build();

        bookingService.updateBooking(updatedBookingDto);
        return "redirect:/bookings/details/" + id;
    }*/

    @GetMapping("/create/{bookingId}/room-availability-update")
    public String showRoomAvailabilityUpdateGet(
            @PathVariable Long bookingId,
            Model model) {

        DetailedBookingDTO booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/customers/all";
        }

        model.addAttribute("booking", booking);
        // You can optionally set defaults for startDate, endDate, numberOfGuests if you want.

        return "detailedBooking";
    }

    @PostMapping("/create/{bookingId}/room-availability-update")
    public String handleAvailabilityForm(@PathVariable Long bookingId,
                                         @RequestParam LocalDate startDate,
                                         @RequestParam LocalDate endDate,
                                         @RequestParam int numberOfGuests,
                                         Model model) {

        // Hämta bokningen
        DetailedBookingDTO booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/customers/all";
        }

        // Hämta tillgängliga rum baserat på de nya datumen och gästerna
        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        // Om det inte finns några tillgängliga rum
        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "No available rooms for the selected dates.");
            return "redirect:/bookings/details/" + bookingId; // Tillbaka till bokningsdetaljer
        }

        // Lägg till nödvändig information till modellen
        model.addAttribute("booking", booking);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("availableRooms", availableRooms);

        return "detailedBooking"; // Till vyn där användaren kan välja ett rum
    }



   /* @PostMapping("/create/{bookingId}/room-availability-update")
    public String showRoomAvailabilityUpdate(@PathVariable Long bookingId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfGuests, Model model) {

        DetailedBookingDTO booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/customers/all";
        }

        DetailedCustomerDto customerDto = customerService.getCustomerById(bookingService.getBookingById(bookingId).getCustomer().getId());


        if (customerDto == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }

        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "There are no available rooms");
            return "redirect:/customers/all";
        }


        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customerDto);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);
        model.addAttribute("booking", booking);

        return "detailedBooking";
    }*/

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
    public String showRoomAvailability(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfGuests, Model model) {

        DetailedCustomerDto customer = customerService.getCustomerById(customerId);

       if (customer == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }

        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

      if (availableRooms.isEmpty()) {
            model.addAttribute("error", "There are no available rooms");
            return "redirect:/customers/all";
        }


        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customer);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);

        return "createBooking";
    }

    @PostMapping("/create/{customerId}")
    public String createBooking(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfGuests, @RequestParam Long roomId, Model model) {

        if (numberOfGuests < 1 || numberOfGuests > 4) {
            model.addAttribute("error", "Number of guests must be between 1 and 4");
            return "createBooking";
        }

        if (!bookingService.checkDates(startDate)){
            model.addAttribute("error", "Please check that entered dates are in the uncertain future and not the unreachable passed.");
            return "redirect:/bookings/all";
        }

        if(!bookingService.checkDateOrder(startDate, endDate)) {
            model.addAttribute("error", "Start date must end date be before end date");
            return "redirect:/bookings/all";
        }

        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        RoomDto room = RoomDto.builder().id(roomId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder().startDate(startDate).endDate(endDate).numberOfGuests(numberOfGuests).customer(customer).room(room).build();

        bookingService.addBooking(booking);
        return "redirect:/bookings/all";
    }

}