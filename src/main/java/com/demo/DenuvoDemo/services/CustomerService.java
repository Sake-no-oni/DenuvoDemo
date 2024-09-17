package com.demo.DenuvoDemo.services;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.customerprojects.repos.CustomerRepo;
import com.demo.DenuvoDemo.customerprojects.repos.ProjectRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Andrii Filimonov
 */
public class CustomerService {
    
    @Autowired
    CustomerRepo customerRepo;
    
    @Autowired
    ProjectRepo projectRepo;
    
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
        updateRelatedCustomerInProjects(customer);
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
    
    private void updateRelatedCustomerInProjects(Customer customer) {
        List<Project> relatedProjects = customer.getAllProjects();
        if (!relatedProjects.isEmpty()) {
            for (Project project: relatedProjects) {
                Project projectInDB = projectRepo.findById(project.getId()).get();
                projectInDB.setRelatedCustomer(customer);
                projectRepo.save(projectInDB);
            }
        }
    }
}
