package com.oycm.controller;

import com.oycm.entity.Customer;
import com.oycm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/getCus")
    public List<Customer> getCustomer() {
        return customerService.getList();
    }
}
