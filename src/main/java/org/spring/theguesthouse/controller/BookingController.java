package org.spring.theguesthouse.controller;

import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.*;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedBookingDTO;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.CustomerService;
import org.spring.theguesthouse.service.RoomService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            return "errorPage";

        }
        model.addAttribute("booking", booking);
        return "detailedBooking";
    }


    @PostMapping("/update/{bookingId}")
    public String updateBooking(@PathVariable Long bookingId,
                                @RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate,
                                @RequestParam Long newRoomId,
                                @RequestParam int numberOfGuests, Model model) {


        DetailedBookingDTO oldBooking = bookingService.getBookingById(bookingId);
        RoomDto oldRoom = oldBooking.getRoom();


        DetailedCustomerDto customerDto = customerService.getCustomerById(bookingService.getBookingById(bookingId).getCustomer().getId());
        CustomerDto cdto = CustomerDto.builder().id(customerDto.getId()).name(customerDto.getName()).build();

        System.out.println("Customers bookingID: " + bookingId);

        System.out.println( "Exsisting: " +
                bookingService.getBookingById(bookingId).toString()
        );

        if (customerDto == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }



        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "There are no available rooms");
            return "redirect:/bookings/details/" + bookingId;
        }

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customerDto);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);


        System.out.println("Customers roomID " + newRoomId);

        DetailedBookingDTO updatedBookingDto = DetailedBookingDTO.builder()
            .id(bookingId)
            .startDate(startDate)
            .endDate(endDate).customer(cdto)
            .numberOfGuests(numberOfGuests)
            .room(roomService.getRoomById(newRoomId))
            .build();

        System.out.println("New booking: " + updatedBookingDto.toString());

        bookingService.updateBooking(updatedBookingDto);
        return "redirect:/bookings/details/" + bookingId;
    }



   @PostMapping("/create/{bookingId}/room-availability-update")
    public String showRoomAvailabilityUpdate(@PathVariable Long bookingId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate,
                                             @RequestParam int numberOfGuests, Model model, RedirectAttributes redirectAttributes) {

        DetailedBookingDTO booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/customers/all";
        }

       if (numberOfGuests < 1 || numberOfGuests > 4) {
           model.addAttribute("error", "Number of guests must be between 1 and 4");
           return showBookingDetails(bookingId, model);
       }

       if (!bookingService.checkDates(startDate)){
           model.addAttribute("error", "Please check that entered dates are in the uncertain future and not the unreachable passed.");
           model.addAttribute("startDate", startDate);
           model.addAttribute("endDate", endDate);
           return showBookingDetails(bookingId, model);
       }

       if(!bookingService.checkDateOrder(startDate, endDate)) {
           model.addAttribute("error", "Start date must come before end date");
           model.addAttribute("startDate", startDate);
           model.addAttribute("endDate", endDate);
           return showBookingDetails(bookingId, model);
       }

       DetailedCustomerDto customerDto = customerService.getCustomerById(bookingService.getBookingById(bookingId).getCustomer().getId());

        if (customerDto == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }

       RoomDto oldRoom = booking.getRoom();
       CustomerDto cdto = CustomerDto.builder().id(customerDto.getId()).name(customerDto.getName()).build();

       //Om gamla rummet är ok med nya datum och antal gäster sparar vi det direkt.
       if (oldRoom.getMaxGuests() == numberOfGuests) {
           System.out.println("Old room can accommodate guests");
           if (roomService.isRoomAvailable(oldRoom.getId(), startDate, endDate, bookingId)) {
               DetailedBookingDTO updatedBooking = DetailedBookingDTO.builder()
                       .id(bookingId).startDate(startDate).endDate(endDate).customer(cdto).numberOfGuests(numberOfGuests).room(oldRoom).build();
               bookingService.updateBooking(updatedBooking);
               redirectAttributes.addFlashAttribute("success", "Booking updated successfully!");
               return "redirect:/bookings/details/" + bookingId;
           }
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
    }

    @GetMapping("/create/{bookingId}/room-availability-update")
    public String showRoomAvailabilityUpdateGet(
            @PathVariable Long bookingId,
            Model model) {

        DetailedBookingDTO booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/bookings/all/" + bookingId;
        }

        model.addAttribute("booking", booking);

        return "redirect:/bookings/details/" + bookingId;
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

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        if (numberOfGuests < 1 || numberOfGuests > 4) {
            model.addAttribute("error", "Number of guests must be between 1 and 4");
            return showCreateBooking(customerId, model);
        }

        if (!bookingService.checkDates(startDate)){
            model.addAttribute("error", "Please check that entered dates are in the uncertain future and not the unreachable passed.");
            return showCreateBooking(customerId, model);
        }

        if(!bookingService.checkDateOrder(startDate, endDate)) {
            model.addAttribute("error", "Start date must be before end date");
            return showCreateBooking(customerId, model);
        }

        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        RoomDto room = RoomDto.builder().id(roomId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder().startDate(startDate).endDate(endDate).numberOfGuests(numberOfGuests).customer(customer).room(room).build();

        bookingService.addBooking(booking);
        return "redirect:/bookings/all";
    }

}