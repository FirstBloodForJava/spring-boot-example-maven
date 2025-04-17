package com.oycm.service;

import com.oycm.dao.CustomerRepository;
import com.oycm.entity.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private static final Log log = LogFactory.getLog(CustomerService.class);
    private final CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getList() {
        Iterable<Customer> iterable = customerRepository.getAll();

        List<Customer> list = new ArrayList<>();
        iterable.forEach(list::add);

        return list;
    }

}
