package com.oycm.dao;

import com.oycm.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Query("select c from Customer c order by c.id")
    List<Customer> getAll();

    Customer findById(long id);
}
