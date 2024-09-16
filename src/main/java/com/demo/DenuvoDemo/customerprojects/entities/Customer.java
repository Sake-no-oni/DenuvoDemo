package com.demo.DenuvoDemo.customerprojects.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Andrii Filimonov
 */

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(length = 255, unique = true)
    private String name;
    
    private String contact;
    
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    
    @OneToMany(
        mappedBy = "relatedCustomer",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<Project> projects = new ArrayList<>();
    
    public Customer() {}
    
    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
        this.creationDate = LocalDateTime.now();
    }
    
    public void addProject(Project project) {
        projects.add(project);
        project.setRelatedCustomer(this);
    }
    
    public void dropProject(Project project) {
        projects.remove(project);
        project.setRelatedCustomer(null);
    }
    
    public List<Project> getAllProjects() {
        return projects;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    public JSONObject toJSONObject() {
        JSONObject customer = new JSONObject();
        
        customer.put("name", name);
        customer.put("contact", contact);
        customer.put("creationDate", creationDate);
        
        return customer;
    }
    
}
