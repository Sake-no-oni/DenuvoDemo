package com.demo.DenuvoDemo.controllers;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.services.CustomerService;
import com.demo.DenuvoDemo.services.ProjectService;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Andrii Filimonov
 */

@Controller
@RequestMapping("customerprojects")
public class DirectAccessController {
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    ProjectService projectService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/getallcustomers", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] getAllCustomers() {
        String status = "ok";
        String description = "Customers acquired successfully";
        
        JSONArray customers = new JSONArray();
        try {
            for (Customer customer : customerService.findAllCustomers()) {
                customers.put(customer.toJSONObject());
            }
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        if (!customers.isEmpty()) response.put("customers", customers);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/getallprojects", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] getAllProjects() {
        String status = "ok";
        String description = "Customers acquired successfully";
        
        JSONArray projects = new JSONArray();
        try {
            for (Project project : projectService.findAllaprojects()) {
                projects.put(project.toJSONObject());
            }
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        if (!projects.isEmpty()) response.put("projects", projects);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/getallprojectsforinterval", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] getAllProjectsForInterval(@RequestParam(required=true) String from, @RequestParam(required=true) String to) {
        String status = "ok";
        String description = "Projects acquired successfully";
        
        JSONArray projectsJson = new JSONArray();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDateTime fromDateTime = LocalDate.parse(from, formatter).atStartOfDay();
            LocalDateTime toDateTime = LocalDate.parse(to, formatter).atStartOfDay();
            
            List<Project> projects = projectService.findProjectsByTimePeriod(fromDateTime, toDateTime);
            for (Project project: projects) {
                JSONObject projectJson = new JSONObject();
                projectJson.put("name", project.getName());
                projectJson.put("description", project.getDescription());
                projectJson.put("creationDate", project.getCreationDate());
                projectJson.put("customer", project.getRelatedCustomer().getName());
                
                projectsJson.put(projectJson);
            }
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        if (!projectsJson.isEmpty()) response.put("projects", projectsJson);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/addcustomer", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] addCustomer(@RequestBody Map<String, String> requestMap) {
        String status = "ok";
        String description = "Customer added successfully";
        
        try {
            String name = requestMap.get("name");
            String contact = requestMap.get("contact");        
            customerService.addCustomer(name, contact);
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/addproject", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] addProject(@RequestBody Map<String, String> requestMap) {
        String status = "ok";
        String description = "Project added successfully";
        
        String customerName = requestMap.get("customerName");
        String projectName = requestMap.get("projectName");
        String projectDescription = requestMap.get("projectDescription");
        
        try {
            Customer customer = customerService.findCustomerByName(customerName);
            if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
            projectService.addProject(projectName, projectDescription, customer);
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/getcustomerdata", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] getCustomerData(@RequestParam(required=true) String customerName) {
        String status = "ok";
        String description = "Customer data acquired";
        
        JSONObject customerData = new JSONObject();        
        try {
            Customer customer = customerService.findCustomerByName(customerName);
            if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
            customerData = customer.toJSONObject();
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description); 
        if (!customerData.isEmpty()) response.put("customerData", customerData);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/getprojectdata", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] getProjectData(@RequestParam(required=true) String projectName) {
        String status = "ok";
        String description = "Project data acquired";
        
        JSONObject projectData = new JSONObject();   
        try {
            Project project = projectService.findProjectByName(projectName);
            if (project == null) throw new NoSuchObjectException("The requested project doesn't exist");
            projectData = project.toJSONObject();
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description); 
        if (!projectData.isEmpty()) response.put("projectData", projectData);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/updatecustomer", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] updateCustomer(@RequestBody Map<String, String> requestMap) {
        String status = "ok";
        String description = "Customer updated successfully";
        
        String customerName = requestMap.get("customerName");
        String customerNewName = requestMap.get("customerNewName");
        String customerNewContact = requestMap.get("customerNewContact");
        
        try {
            Customer customer = customerService.findCustomerByName(customerName);
            if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
            customerService.updateCustomerName(customer.getId(), customerNewName);
            customerService.updateCustomerContact(customer.getId(), customerNewContact);
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/updateproject", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] updateProject(@RequestBody Map<String, String> requestMap) {
        String status = "ok";
        String description = "Project updated successfully";
        
        String projectName = requestMap.get("projectName");
        String projectNewName = requestMap.get("projectNewName");
        String projectDescription = requestMap.get("projectDescription");
        
        try {
            Project prtoject = projectService.findProjectByName(projectName);
            if (prtoject == null) throw new NoSuchObjectException("The requested project doesn't exist");
            projectService.updateProjectName(prtoject.getId(), projectNewName);
            projectService.updateProjectDescription(prtoject.getId(), projectDescription);
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/dropcustomer", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] dropCustomer(@RequestParam(required=true) String customerName) {
        String status = "ok";
        String description = "Customer dropped successfully";
        
        try {
            Customer customer = customerService.findCustomerByName(customerName);
            if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
            customerService.dropCustomer(customer.getId());
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/dropproject", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    byte[] dropProject(@RequestParam(required=true) String projectName) {
        String status = "ok";
        String description = "Project dropped successfully";
        
        try {
            Project project = projectService.findProjectByName(projectName);
            if (project == null) throw new NoSuchObjectException("The requested project doesn't exist");
            projectService.dropProject(project.getId());
        } catch (Exception e) {
            status = "error";
            description = e.getMessage();
        }
        
        JSONObject response = new JSONObject();
        response.put("status", status);
        response.put("description", description);
        
        return response.toString().getBytes();
    }
    
}
