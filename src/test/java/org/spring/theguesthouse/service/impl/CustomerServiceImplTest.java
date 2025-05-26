package org.spring.theguesthouse.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.entity.Booking;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
class CustomerServiceImplTest {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private CustomerService customerService;

    private String trueName = "Maja Gräddnos";
    private String falseName = "Süleyman den Store";

    @BeforeEach
    public void setUp() {
        Room r1 = Room.builder().roomNumber(101).maxGuests(1).build();
        Room r2 = Room.builder().roomNumber(102).maxGuests(2).build();
        Room r3 = Room.builder().roomNumber(103).maxGuests(3).build();

        roomRepo.save(r1);
        roomRepo.save(r2);
        roomRepo.save(r3);

        Customer c1 = Customer.builder().name(trueName).tel("018-225162").build();
        Customer c2 = Customer.builder().name("Gammel-Maja").tel("018-225163").build();
        Customer c3 = Customer.builder().name("Gullan von Arkadien").tel("018-225164").build();

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
    void getAllCustomers() {
        List<CustomerDto> allCustomers = customerService.getAllCustomers();
        assertTrue(allCustomers.size() == 3);
        assertTrue(allCustomers.stream().map(c -> c.getName()).toList().contains(trueName));
        assertFalse(allCustomers.stream().map(c -> c.getName()).toList().contains(falseName));
    }

    @Test
    void detailedCustomerDtoToCustomer() {
    }

    @Test
    void customerToCustomerDto() {
    }

    @Test
    void customerToDetailedCustomerDto() {
    }

    @Test
    void getCustomerById() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomer() {
    }
}