package com.demo.DenuvoDemo.services;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.repos.CustomerRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Andrii Filimonov
 */
public class CustomerService {
    
    @Autowired
    CustomerRepo customerRepo;
    
    public void addCustomer(Customer customer) {
        customerRepo.save(customer);
    }
    
    public void addCustomer(String name, String contacts) {
        customerRepo.save(new Customer(name, contacts));
    }
    
    public Customer findCustomerByName(String name) {
        return customerRepo.findByName(name);
    }
    
    public List<Customer> findAllCustomers() {
        return customerRepo.findAll();
    }
    
    public void updateCustomerName(Long customerId, String newName) {
        Customer customer = customerRepo.findById(customerId).get();
        customer.setName(newName);
        customerRepo.save(customer);
    }
    
    public void updateCustomerContact(Long customerId, String newContact) {
        Customer customer = customerRepo.findById(customerId).get();
        customer.setContact(newContact);
        customerRepo.save(customer);
    }
    
    public void dropCustomer(Long customerId) {
        Customer customer = customerRepo.findById(customerId).get();
        customerRepo.delete(customer);
    }
}
