package org.spring.theguesthouse.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private RoomRepo roomRepo;

    @BeforeEach
    public void setUp() {
        Room r1 = Room.builder().roomNumber(101).maxGuests(1).build();
        Room r2 = Room.builder().roomNumber(102).maxGuests(2).build();
        Room r3 = Room.builder().roomNumber(103).maxGuests(3).build();

        roomRepo.save(r1);
        roomRepo.save(r2);
        roomRepo.save(r3);

        Customer c1 = Customer.builder().name("Maja Gräddnos").email("maja@asgrand.ua").build();
        Customer c2 = Customer.builder().name("Gammel-Maja").email("maja@domkyrkotornet.ua").build();
        Customer c3 = Customer.builder().name("Gullan von Arkadien").email("gullan@arkadien.ua").build();

        customerRepo.save(c1);
        customerRepo.save(c2);
        customerRepo.save(c3);

        Booking b1 = Booking.builder().customer(c1)
                .startDate(LocalDate.of(2025, 10, 15))
                .endDate(LocalDate.of(2025, 10, 18)).numberOfGuests(1).room(r1).build();

        Booking b2 = Booking.builder().customer(c1)
                .startDate(LocalDate.of(2025, 10, 2))
                .endDate(LocalDate.of(2025, 10, 3)).numberOfGuests(2).room(r2).build();

        Booking b3 = Booking.builder().customer(c3)
                .startDate(LocalDate.of(2025, 8, 15))
                .endDate(LocalDate.of(2025, 9, 1)).numberOfGuests(3).room(r3).build();

        bookingRepo.save(b1);
        bookingRepo.save(b2);
        bookingRepo.save(b3);
    }

    @Test
    void showAllCustomers() throws Exception {
        this.mockMvc.perform(get("/customers/all")).andExpect(status().isOk())
                .andExpect(model().attributeExists("allCustomers"))
                .andExpect(view().name("showAllCustomers"));
    }

    @Test
    void createCustomer() throws Exception {
        this.mockMvc.perform(post("/customers/create")
                .param("name", "Kalle Huggorm")
                .param("email", "Huggorm@Slottsbacken.ua"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers/all"));

        this.mockMvc.perform(post("/customers/create")
                        .param("name", "Kalle Huggorm")
                        .param("email", "Huggorm@Slottsbacken."))
                .andExpect(view().name("showAllCustomers"));
    }

    @Test
    void showCustomerDetails() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomerById() {
    }
}