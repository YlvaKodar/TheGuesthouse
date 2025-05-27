package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.CustomerDto;
import org.spring.theguesthouse.dto.DeleteResponseDto;
import org.spring.theguesthouse.dto.DetailedCustomerDto;
import org.spring.theguesthouse.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createCustomer(@RequestParam String name, @RequestParam String email, Model model) {

        model.addAttribute("inputName", name);
        model.addAttribute("inputEmail", email);

        if (customerService.customerExist(name, email)){
            model.addAttribute("nameError", "A customer with this name and contact information already exists");
            return showAllCustomers(model);
        }

        if (!customerService.legitName(name)){
            model.addAttribute("nameError", "Enter first name and surname at min 2 characters each, seperated by space.");
            return showAllCustomers(model);
        }

        if (!customerService.legitEmail(email)){
            model.addAttribute("emailError", "Enter valid email address");
            return showAllCustomers(model);
        }

        customerService.addCustomer(DetailedCustomerDto.builder().name(name).email(email).build());

        return "redirect:/customers/all";
    }

    @GetMapping("/details/{id}")
    public String showCustomerDetails(@PathVariable Long id, Model model) {
        DetailedCustomerDto customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "detailedCustomer";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, @RequestParam String name, @RequestParam String email, Model model) {

        if (!customerService.legitName(name)){
            model.addAttribute("nameError", "Enter first name and surname at min 2 characters each, seperated by space.");
            return showCustomerDetails(id, model);
        }

        if (!customerService.legitEmail(email)){
            model.addAttribute("emailError", "Enter valid email address");
            return showCustomerDetails(id, model);
        }

        DetailedCustomerDto updatedCustomer = DetailedCustomerDto.builder()
                .id(id).name(name).email(email).build();

        customerService.updateCustomer(updatedCustomer);
        return "redirect:/customers/details/" + id;
    }

    @RequestMapping(path = "/deleteById/{id}")
    public String deleteCustomerById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        DeleteResponseDto customer = customerService.deleteCustomerById(id);

        if(!customer.isSuccess()){
            model.addAttribute("deleteError", customer.getMessage());
            return showAllCustomers(model);
        }

        redirectAttributes.addFlashAttribute("deleteConfirmation", customer.getMessage());
        return "redirect:/customers/all";
    }
}
