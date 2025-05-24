package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DeleteResponseDto;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c);

    DetailedCustomerDto customerToDetailedCustomerDto(Customer c);

    CustomerDto customerToCustomerDto(Customer c);

    DeleteResponseDto deleteCustomerById(Long id);

    List<CustomerDto> getAllCustomers();

    DetailedCustomerDto getCustomerById(Long id);

    DetailedCustomerDto addCustomer (DetailedCustomerDto customer);

    DetailedCustomerDto updateCustomer(DetailedCustomerDto customer);


}
