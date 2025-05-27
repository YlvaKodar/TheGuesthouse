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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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

    //ÄR DET HÄR VI ÄR NÄR VI KOMMER FRÅN DETAILED BOOKING TILL UPDATE? DÅ VILL VI JU HA IN FLER VÄRDEN,
    //LÄMPLIGTVIS HELA BOOKING-DTO
    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                @RequestParam int numberOfGuests, Model model) {


        DetailedBookingDTO updatedBooking = DetailedBookingDTO.builder()
                .id(id)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .numberOfGuests(numberOfGuests)
                .build();

        bookingService.updateBooking(updatedBooking);
        return "redirect:/bookings/details/" + id;
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
    public String showRoomAvailability(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfGuests, Model model, Errors errors) {

        DetailedCustomerDto customer = customerService.getCustomerById(customerId);
        List<RoomDto> availableRooms = roomService.getAllAvailableRooms(startDate, endDate, numberOfGuests);

        //TEMPORÄRT ANROP TILL ALLA CHECKAT VI SKA GÖRA INNAN VI SKA SKAPA KUND
        //OBS VI SKA JU INTE HA SÅHÄR PGA SKA JU GÖRA RETURN OM ERROR
        allChecks(customer, startDate, endDate, numberOfGuests, model, availableRooms, null,false);


        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("customer", customer);
        model.addAttribute("startDate", startDate);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("endDate", endDate);

        return "createBooking";
    }

    @PostMapping("/create/{customerId}")
    public String createBooking(@PathVariable Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfGuests, @RequestParam Long roomId, Model model, Errors errors) {

        if (numberOfGuests < 1 || numberOfGuests > 4) {
            model.addAttribute("error", "Number of guests must be between 1 and 4");
            return "createBooking";
        }

        CustomerDto customer = CustomerDto.builder().id(customerId).build();
        RoomDto room = RoomDto.builder().id(roomId).build();
        DetailedBookingDTO booking = DetailedBookingDTO.builder().startDate(startDate).endDate(endDate).numberOfGuests(numberOfGuests).customer(customer).room(room).build();

        bookingService.addBooking(booking);
        return "redirect:/bookings/all";
    }

    public String allChecks(DetailedCustomerDto customer, LocalDate startDate, LocalDate endDate, int numberOfGuests,
                            Model model, List<RoomDto> availableRooms, Long currentRoom, Boolean update) {
        //OBS: ALLA dessa checkar är checkar och valideringar som ska göras av form
        //innan man kan uppdatera en kund. Jag vet inte riktigt vad vi ska lägga dem (ett eller flera ställen?),
        //vad vi ska returna etc, för jag är osäker på template-flödet.
        //Lägger här så kan vi diskutera.

        if (customer == null) {
            model.addAttribute("error", "Could not get customer id. Please try again");
            return "redirect:/customers/all";
        }


        if (numberOfGuests < 1 || numberOfGuests > 4) {
            model.addAttribute("error", "Number of guests must be between 1 and 4");
            return "redirect:/bookings/all";
        }

        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "There are no available rooms");
            return "redirect:/bookings/all";
        }

        if (!bookingService.checkDates(startDate)){
            model.addAttribute("error", "Please check that entered dates are in the uncertain future and not the unreachable passed.");
            return "redirect:/bookings/all";
        }

        if(!bookingService.checkDateOrder(startDate, endDate)) {
            model.addAttribute("error", "Start date must end date be before end date");
            return "redirect:/bookings/all";
        }

        //Nåt annat vi vill checka?
        return "Trams!";

    }

}