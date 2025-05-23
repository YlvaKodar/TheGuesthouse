package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DeleteCustomerResponseDto;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.service.BookingService;
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final BookingService bookingService;

    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c) {
        return Customer.builder().id(c.getId()).name(c.getName()).tel(c.getTel()).build();
    }


    public CustomerDto customerToCustomerDto(Customer c) {
        return CustomerDto.builder().id(c.getId()).name(c.getName()).build();
    }

    public DetailedCustomerDto customerToDetailedCustomerDto(Customer c) {
        return DetailedCustomerDto.builder().id(c.getId()).name(c.getName()).tel(c.getTel())
                .bookings(c.getBookings().stream().map(bookingService::bookingToDto).toList())
                .build();
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepo.findAll().stream().map(this::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public DetailedCustomerDto getCustomerById(Long id) {
        return customerRepo.findById(id).map(this::customerToDetailedCustomerDto).orElse(null);
    }

    @Override
    public DetailedCustomerDto addCustomer(DetailedCustomerDto customer) {
        Customer savedCustomer = customerRepo.save(detailedCustomerDtoToCustomer(customer));
        return customerToDetailedCustomerDto(savedCustomer);
    }


    @Override
    public DeleteCustomerResponseDto deleteCustomerById (Long customerId) {
        return customerRepo.findById(customerId)
                .map(customer -> {
                    if (!customer.getBookings().isEmpty()) {
                        return new DeleteCustomerResponseDto(false, "Customer cannot be deleted as it has a booking");
                    }else {
                    customerRepo.deleteById(customerId);
                    return new DeleteCustomerResponseDto(true, "Customer has been deleted");}
                })
                .orElse(new DeleteCustomerResponseDto(false, "Customer does not exist"));
    }

    @Override
    public DetailedCustomerDto updateCustomer(DetailedCustomerDto customerDto) {
        Customer existingCustomer = customerRepo.findById(customerDto.getId()).orElseThrow(() -> new RuntimeException("Customer with id" + customerDto.getId() + " not found"));

        existingCustomer.setName(customerDto.getName());
        existingCustomer.setTel(customerDto.getTel());
        Customer updatedCustomer = customerRepo.save(existingCustomer);

        return customerToDetailedCustomerDto(updatedCustomer);
    }

}
