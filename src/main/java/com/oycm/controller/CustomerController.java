package com.oycm.controller;

import com.oycm.entity.Customer;
import com.oycm.event.EmailService;
import com.oycm.service.CustomerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private EmailService emailService;
    private CustomerService customerService;
    private static Log log = LogFactory.getLog(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/getCus")
    public List<Customer> getCustomer(HttpServletRequest request) {
        log.info(request.getRequestURI());
        emailService.sendEmail(request.getRequestURI(), "sendEmail");
        return customerService.getList();
    }
}
