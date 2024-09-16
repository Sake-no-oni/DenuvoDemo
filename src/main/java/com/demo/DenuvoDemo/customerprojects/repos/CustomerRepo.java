package com.demo.DenuvoDemo.customerprojects.repos;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Andrii Filimonov
 */

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    
    Customer findByName(String name);
    
}
