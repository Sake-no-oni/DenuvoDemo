package com.demo.DenuvoDemo.customerprojects.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.json.JSONObject;

/**
 *
 * @author Andrii Filimonov
 */

@Entity
@Table(name = "projects")
public class Project {
    
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(length = 255)
    private String name;
    
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "related_customer")
    private Customer relatedCustomer;
    
    public Project() {}
    
    public Project(String name, String description, Customer relatedCustomer) {
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.relatedCustomer = relatedCustomer;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getRelatedCustomer() {
        return relatedCustomer;
    }

    public void setRelatedCustomer(Customer relatedCustomer) {
        this.relatedCustomer = relatedCustomer;
    }
    
    public JSONObject toJSONObject() {
        JSONObject project = new JSONObject();
        
        project.put("name", name);
        project.put("description", description);
        project.put("creationDate", creationDate);
        
        return project;
    }
    
}
