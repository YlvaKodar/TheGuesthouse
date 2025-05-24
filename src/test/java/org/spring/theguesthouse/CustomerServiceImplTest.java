package org.spring.theguesthouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.entity.Customer;
import org.spring.theguesthouse.repository.CustomerRepo;
import org.spring.theguesthouse.service.impl.CustomerServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest{

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void addCustomerAndShowSuccessMessage(){
        DetailedCustomerDto dto = DetailedCustomerDto.builder().id(1L).name("Alice").tel("12345").build();

        Customer customer= Customer.builder().id(1L).name("Alice").tel("12345").build();

        when(customerRepo.save(any(Customer.class))).thenReturn(customer);
        DetailedCustomerDto feedback = customerService.addCustomer(dto);
        assertNotNull(feedback);
        assertEquals("Alice", feedback.getName());
        assertEquals("12345", feedback.getTel());

        verify(customerRepo, times(1)).save(any(Customer.class));
    }

   /* @Test
    void deleteCustomerAndShowSuccessMessage(){
        Long Id = 1L;

        doNothing().when(customerRepo).deleteById(Id);
        String feedback = customerService.deleteCustomerById(Id);
        assertEquals("Customer was successfully deleted", feedback);
    }*/
}


