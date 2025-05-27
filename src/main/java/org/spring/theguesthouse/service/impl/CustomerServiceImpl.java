package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DeleteResponseDto;
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


    private final String validName = "^[a-zA-ZåäöÅÄÖ]{2,}\\s[a-zA-ZåäöÅÄÖ]{2,}$"; //Name: min 2 char, space, 2 char.
    private final String validEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c) {
        return Customer.builder().id(c.getId()).name(c.getName()).email(c.getEmail()).build();
    }


    public CustomerDto customerToCustomerDto(Customer c) {
        return CustomerDto.builder().id(c.getId()).name(c.getName()).build();
    }

    public DetailedCustomerDto customerToDetailedCustomerDto(Customer c) {
        return DetailedCustomerDto.builder().id(c.getId()).name(c.getName()).email(c.getEmail())
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
    public DeleteResponseDto deleteCustomerById (Long customerId) {
        return customerRepo.findById(customerId)
                .map(customer -> {
                    if (!customer.getBookings().isEmpty()) {
                        return new DeleteResponseDto(false, "Customer with id " + customerId  + " cannot be deleted as it has a booking");
                    }else {
                    customerRepo.deleteById(customerId);
                    return new DeleteResponseDto(true, "Customer with id " + customerId  + " has been deleted");}
                })
                .orElse(new DeleteResponseDto(false, "Customer with id " + customerId  + " does not exist"));
    }

    @Override
    public DetailedCustomerDto updateCustomer(DetailedCustomerDto customerDto) {
        Customer existingCustomer = customerRepo.findById(customerDto.getId()).orElseThrow(() -> new RuntimeException("Customer with id" + customerDto.getId() + " not found"));

        existingCustomer.setName(customerDto.getName());
        existingCustomer.setEmail(customerDto.getEmail());
        Customer updatedCustomer = customerRepo.save(existingCustomer);

        return customerToDetailedCustomerDto(updatedCustomer);
    }

    @Override
    public Boolean customerExist(String name, String email) {
        return getAllCustomers().stream()
                .filter(customer -> customer.getName().equals(name))
                .anyMatch(customer -> getCustomerById(customer.getId()).getEmail().equals(email));
    }

    @Override
    public Boolean legitName(String name) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        return name.matches(validName);
    }

    @Override
    public Boolean legitEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches(validEmail);
    }

}
