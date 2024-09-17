package com.demo.DenuvoDemo.services;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.customerprojects.repos.CustomerRepo;
import com.demo.DenuvoDemo.customerprojects.repos.ProjectRepo;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Andrii Filimonov
 */

@SpringBootTest
public class CustomerServiceTest {
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    ProjectService projectService;
    
    @Autowired
    CustomerRepo customerRepo;
    
    @Autowired
    ProjectRepo projectRepo;
    
    static String CUST_A_NAME = "A";
    static String CUST_A_CONTACT = "a@mail.com";
    
    static String CUST_B_NAME = "B";
    static String CUST_B_CONTACT = "b@mail.com";
    
    static String PROJECT_A1_NAME = "A1";
    static String PROJECT_A1_DESCRIPTION = "Project 1 for customer A";
    
    static String PROJECT_A2_NAME = "A2";
    static String PROJECT_A2_DESCRIPTION = "Project 2 for customer A";
    
    static String PROJECT_B1_NAME = "B1";
    static String PROJECT_B1_DESCRIPTION = "Project 1 for customer B";
    
    @BeforeEach
    public void prepareDB() {
        customerRepo.deleteAll();
        projectRepo.deleteAll();
        
        Customer custA = new Customer(CUST_A_NAME, CUST_A_CONTACT);
        customerRepo.save(custA);
        
        Customer custB = new Customer(CUST_B_NAME, CUST_B_CONTACT);
        customerRepo.save(custB);
        
        projectRepo.save(new Project(PROJECT_A1_NAME, PROJECT_A1_DESCRIPTION, custA));
        projectRepo.save(new Project(PROJECT_A2_NAME, PROJECT_A2_DESCRIPTION, custA));
        projectRepo.save(new Project(PROJECT_B1_NAME, PROJECT_B1_DESCRIPTION, custB));
    }
    
    @Test
    public void shouldAddCustomer() {
        String custName = "C";
        String custContact = "c@mail.com";
        
        customerService.addCustomer(custName, custContact);
        
        Assertions.assertEquals(custName, customerService.findCustomerByName(custName).getName());
        Assertions.assertEquals(custContact, customerService.findCustomerByName(custName).getContact());
    }
    
    @Test
    public void shouldGetAllCustomers() {
        Assertions.assertEquals(2, customerService.findAllCustomers().size());
    }
    
    @Test
    public void shouldGetProjectsByCustomer() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        List<Project> projects = projectService.getProjectsByCustomerId(cust.getId());
        
        List<String> namespace = List.of(PROJECT_A1_NAME, PROJECT_A2_NAME);
        for (Project project: projects) {
            Assertions.assertTrue((namespace.contains(project.getName())));
        }
    }
    
    @Test
    public void shouldGetCustomerByProject() {
        Customer cust = projectService.findProjectByName(PROJECT_A1_NAME).getRelatedCustomer();        
        Assertions.assertEquals(CUST_A_NAME, cust.getName());
    }
    
    @Test
    public void shouldUpdateCustomerName() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        String custNewName = cust.getName() + "_NEW";
        
        customerService.updateCustomerName(cust.getId(), custNewName);
        
        Assertions.assertEquals(cust.getId(), customerRepo.findByName(custNewName).getId());
        for (Project project: projectService.getProjectsByCustomerId(cust.getId())) {
            Assertions.assertEquals(custNewName, project.getRelatedCustomer().getName());
        }
    }
    
    @Test
    public void shouldUpdateCustomerContact() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        String custNewContact = cust.getContact() + "_NEW";
        
        customerService.updateCustomerContact(cust.getId(), custNewContact);
        
        Assertions.assertEquals(custNewContact, customerRepo.findByName(CUST_A_NAME).getContact());
    }
    
    @Test
    public void shouldCascadeDropCustomerAndRelatedProjects() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        customerService.dropCustomer(cust.getId());
        
        Assertions.assertNull(customerRepo.findByName(CUST_A_NAME));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> projectService.getProjectsByCustomerId(cust.getId())
        );
    }
}
