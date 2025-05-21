package org.spring.theguesthouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c) {
        return Customer.builder().id(c.getId()).name(c.getName()).tel(c.getTel()).build();
    }

    @Override
    public String addCustomer(DetailedCustomerDto customer) {
        customerRepo.save(detailedCustomerDtoToCustomer(customer));
        return "Customer was successfully added";
    }

    @Override
    public String deleteCustomerById(Long customerId) {
        if (!customerRepo.existsById(customerId)) {
            return "Customer not found";
        }
        customerRepo.deleteById(customerId);
        return "Customer was successfully deleted";
    }

    @Override
    public String updateCustomer(DetailedCustomerDto customer) {
        return "";
    }

}
