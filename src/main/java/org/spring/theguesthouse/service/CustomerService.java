package org.spring.theguesthouse.service;

import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DeleteCustomerResponseDto;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c);

    DetailedCustomerDto customerToDetailedCustomerDto(Customer c);

    CustomerDto customerToCustomerDto(Customer c);

    DeleteCustomerResponseDto deleteCustomerById(Long id);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    DetailedCustomerDto addCustomer (DetailedCustomerDto customer);

    DetailedCustomerDto updateCustomer(DetailedCustomerDto customer);


}
