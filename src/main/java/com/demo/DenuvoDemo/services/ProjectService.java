package com.demo.DenuvoDemo.services;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.customerprojects.repos.CustomerRepo;
import com.demo.DenuvoDemo.customerprojects.repos.ProjectRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Andrii Filimonov
 */
public class ProjectService {
    
    @Autowired
    ProjectRepo projectRepo;
    
    @Autowired
    CustomerRepo customerRepo;
    
    public void addProject(Project project) {
        projectRepo.save(project);
    }
    
    public void addProject(String name, String description, Customer customer) {
        projectRepo.save(new Project(name, description, customer));
    }
    
    public Project findProjectByName(String name) {
        return projectRepo.findByName(name);
    }
    
    public List<Project> findAllaprojects() {
        return projectRepo.findAll();
    }
    
    public List<Project> getProjectsByCustomerId(Long customerId) {
        Customer customer = customerRepo.findById(customerId).get();
        return customer.getAllProjects();
    }
    
    public List<Project> findProjectsByTimePeriod(LocalDateTime from, LocalDateTime to) {
        return projectRepo.findAllByCreationDateBetween(from, to);
    }
    
    public void updateProjectName(Long projectId, String newName) {
        Project project = projectRepo.findById(projectId).get();
        project.setName(newName);
        projectRepo.save(project);
    }
    
    public void updateProjectDescription(Long projectId, String newDescription) {
        Project project = projectRepo.findById(projectId).get();
        project.setDescription(newDescription);
        projectRepo.save(project);
    }
    
    public void dropProject(Long projectId) {
        Project project = projectRepo.findById(projectId).get();
        project.setRelatedCustomer(null);
        projectRepo.delete(project);
    }
    
}
