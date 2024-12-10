package com.oycm.service;

import com.oycm.dao.CustomerRepository;
import com.oycm.entity.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private static final Log log = LogFactory.getLog(CustomerService.class);
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public CustomerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> getList() {
        String sql = "SELECT * FROM CUSTOMER";
        jdbcTemplate.queryForList(sql).forEach(row -> {
            System.out.println(row);
        });
        return jdbcTemplate.queryForList(sql);
    }

    @Bean
    CommandLineRunner myCommandLineRunner(CustomerRepository repository) {

        return (String... args) -> {
            // save a few customers
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(customer -> {
                log.info(customer.toString());
            });
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");
        };
    }
}
