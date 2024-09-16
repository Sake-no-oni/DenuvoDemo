package com.demo.DenuvoDemo.services;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.customerprojects.repos.CustomerRepo;
import com.demo.DenuvoDemo.customerprojects.repos.ProjectRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 *
 * @author Andrii Filimonov
 */

@SpringBootTest
public class ProjectServiceTest {
    
    @Autowired
    ProjectService projectService;
    
    @Autowired
    CustomerRepo customerRepo;
    
    @Autowired
    ProjectRepo projectRepo;
    
    static String CUST_A_NAME = "A";
    static String CUST_A_CONTACT = "a@mail.com";
    
    static String PROJECT_A1_NAME = "A1";
    static String PROJECT_A1_DESCRIPTION = "Project 1 for customer A";
    
    static String PROJECT_A2_NAME = "A2";
    static String PROJECT_A2_DESCRIPTION = "Project 2 for customer A";
    
    @BeforeEach
    public void prepareDB() {
        customerRepo.deleteAll();
        projectRepo.deleteAll();
        
        Customer custA = new Customer(CUST_A_NAME, CUST_A_CONTACT);
        customerRepo.save(custA);
        
        projectRepo.save(new Project(PROJECT_A1_NAME, PROJECT_A1_DESCRIPTION, custA));
        projectRepo.save(new Project(PROJECT_A2_NAME, PROJECT_A2_DESCRIPTION, custA));
    }
    
    @Test
    public void shouldAddProject() {
        String prName = "B2";
        String prDescr = "Project 2 for customer B";
        Customer cust = customerRepo.findByName("B");
        
        projectService.addProject(prName, prDescr, cust);
        
        Assertions.assertEquals(prName, projectService.findProjectByName(prName).getName());
    }
    
    @Test
    public void shouldFailAddingProjectDueToNonExistentCustomer() {
        String prName = "B2";
        String prDescr = "Project 2 for customer B";        
        Customer cust = new Customer("C", "c@mail.com");
        
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
            () -> projectService.addProject(prName, prDescr, cust)
        );
    }
    
    @Test
    public void shoudGetAllProjects() {
        Assertions.assertEquals(2, projectService.findAllaprojects().size());
    }
    
    @Test
    public void shouldGetAllProjectsForCustomer() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        List<Project> projects = projectService.getProjectsByCustomerId(cust.getId());
        
        Assertions.assertEquals(2, projects.size());
    }
    
    @Test
    public void shouldGetProjectsForPeriod() {
        Customer cust = customerRepo.findByName(CUST_A_NAME);
        
        Project prInsidePeriod = new Project("Inside", "Project inside time period", cust);
        prInsidePeriod.setCreationDate(LocalDateTime.of(2024, 8, 11, 0, 0));
        projectRepo.save(prInsidePeriod);
       
        Project prOutsidePertiod = new Project("Outside", "Project outside of time period", cust);
        prOutsidePertiod.setCreationDate(LocalDateTime.of(2024, 8, 9, 0, 0));
        projectRepo.save(prOutsidePertiod);
        
        LocalDateTime from = LocalDateTime.of(2024, 8, 10, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 8, 12, 0, 0);
        
        List<Project> projects = projectService.findProjectsByTimePeriod(from, to);
        
        Assertions.assertEquals(1, projects.size());
        Assertions.assertEquals(prInsidePeriod.getId(), projects.get(0).getId());
    }
    
    @Test
    public void shouldUpdateProjectName() {
        Project pr = projectRepo.findByName(PROJECT_A1_NAME);
        String prNewName = pr.getName() + "_NEW";
        
        projectService.updateProjectName(pr.getId(), prNewName);
        
        Assertions.assertEquals(pr.getId(), projectRepo.findByName(prNewName).getId());
    }
    
    @Test
    public void shouldUpdateProjectDescription() {
        Project pr = projectRepo.findByName(PROJECT_A1_NAME);
        String prNewDescr = pr.getName() + " NEW";
        
        projectService.updateProjectDescription(pr.getId(), prNewDescr);
        
        Assertions.assertEquals(prNewDescr, projectRepo.findByName(PROJECT_A1_NAME).getDescription());
    }
    
    @Test
    public void shouldDropProject() {
        Project pr = projectRepo.findByName(PROJECT_A1_NAME);
        projectService.dropProject(pr.getId());
        
        Assertions.assertNull(projectRepo.findByName(PROJECT_A1_NAME));
    }
    
}
