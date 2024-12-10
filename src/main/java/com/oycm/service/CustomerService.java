package com.oycm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

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
    CommandLineRunner myCommandLineRunner() {

        return (String... args) -> {
            String sql = "INSERT INTO CUSTOMER (ID, FIRST_NAME, LAST_NAME, EMAIL, AGE) VALUES (5, 'John', 'Doe', 'john.doe@example.com', 30)";
            jdbcTemplate.update(sql);
        };
    }
}
