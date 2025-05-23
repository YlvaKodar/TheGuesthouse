package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path ="/customers")
@RequiredArgsConstructor
public class CustomerController {

    public final CustomerService customerService;

    //localhost:8080/customers/all
    @RequestMapping("/all")
    public String showAllCustomers(Model model) {
        List<CustomerDto> customerList = customerService.getAllCustomers();
        model.addAttribute("customerTitle", "Customers");
        model.addAttribute("allCustomers", customerList);
        model.addAttribute("id", "ID");
        model.addAttribute("name", "Name");
        return "showAllCustomers";
    }

    @PostMapping("/create")
    public String createCustomer(@RequestParam String name, @RequestParam String tel, Model model) {
        customerService.addCustomer(DetailedCustomerDto.builder().name(name).tel(tel).build());
        return "redirect:/customers/all";
    }

    @RequestMapping(path = "/deleteById/{id}")
    public String deleteBookingById(@PathVariable Long id, Model model) {
        customerService.deleteCustomerById(id);
        return "redirect:/customers/all";
    }

}
