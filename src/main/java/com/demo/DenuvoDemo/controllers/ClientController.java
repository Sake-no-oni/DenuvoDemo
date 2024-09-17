package com.demo.DenuvoDemo.controllers;

import com.demo.DenuvoDemo.customerprojects.entities.Customer;
import com.demo.DenuvoDemo.customerprojects.entities.Project;
import com.demo.DenuvoDemo.services.CustomerService;
import com.demo.DenuvoDemo.services.ProjectService;
import com.demo.DenuvoDemo.utils.ZipUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Andrii Filimonov
 */

@Controller
@RequestMapping("client")
public class ClientController {
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    ProjectService projectService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String listCustomersAndProjects(Model model) {
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("projects", projectService.findAllaprojects());
        return "customer-list";
    }

    @RequestMapping(method = RequestMethod.GET,  value = "/addcustomer")
    public String addCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-adding-form";
    }

    @RequestMapping(method = RequestMethod.POST,  value = "/addcustomer")
    public String addCustomer(@ModelAttribute Customer customer) {
        customer.setCreationDate(LocalDateTime.now());
        customerService.addCustomer(customer);
        return "redirect:/client";
    }

    @RequestMapping(method = RequestMethod.GET,  value = "/editcustomer/{name}")
    public String editCustomerForm(@PathVariable String name, Model model) {
        model.addAttribute("customer", customerService.findCustomerByName(name));
        return "customer-editing-form";
    }

    @RequestMapping(method = RequestMethod.POST,  value = "/editcustomer/{name}")
    public String editCustomer(@PathVariable String name, @ModelAttribute Customer customerNew) throws NoSuchObjectException {
        Customer customer = customerService.findCustomerByName(name);
        if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
        customerService.updateCustomerName(customer.getId(), customerNew.getName());
        customerService.updateCustomerContact(customer.getId(), customerNew.getContact());                
        return "redirect:/client";
    }

    @RequestMapping(method = RequestMethod.GET,  value = "/deletecustomer/{name}")
    public String deleteCustomer(@PathVariable String name) throws NoSuchObjectException {
        Customer customer = customerService.findCustomerByName(name);
        if (customer == null) throw new NoSuchObjectException("The requested customer doesn't exist");
        customerService.dropCustomer(customer.getId());
        return "redirect:/client";
    }
    
    @RequestMapping(method = RequestMethod.GET,  value = "/addproject")
    public String addProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("customers", customerService.findAllCustomers());
        return "project-adding-form";
    }

    @RequestMapping(method = RequestMethod.POST,  value = "/addproject")
    public String addProject(@ModelAttribute Project project) throws NoSuchObjectException {
        Customer customer = customerService.findCustomerByName(project.getRelatedCustomer().getName());
        if (customer == null) throw new NoSuchObjectException("The corresponding customer doesn't exist");
        project.setCreationDate(LocalDateTime.now());
        project.setRelatedCustomer(customer);
        projectService.addProject(project);
        return "redirect:/client";
    }
    
    @RequestMapping(method = RequestMethod.GET,  value = "/editproject/{name}")
    public String editProjectForm(@PathVariable String name, Model model) {
        model.addAttribute("project", projectService.findProjectByName(name));
        return "project-editing-form";
    }

    @RequestMapping(method = RequestMethod.POST,  value = "/editproject/{name}")
    public String editProject(@PathVariable String name, @ModelAttribute Project projectNew) throws NoSuchObjectException {
        Project project = projectService.findProjectByName(name);
        if (project == null) throw new NoSuchObjectException("The requested project doesn't exist");
        projectService.updateProjectName(project.getId(), projectNew.getName());
        projectService.updateProjectDescription(project.getId(), projectNew.getDescription());
        return "redirect:/client";
    }
    
    @RequestMapping(method = RequestMethod.GET,  value = "/deleteproject/{name}")
    public String deleteProject(@PathVariable String name) throws NoSuchObjectException {
        Project project = projectService.findProjectByName(name);
        if (project == null) throw new NoSuchObjectException("The requested project doesn't exist");
        projectService.dropProject(project.getId());
        return "redirect:/client";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/downloadprojectsforperiod", produces="application/zip")
    public ResponseEntity<InputStreamResource> downloadProjectsZip(@RequestParam("from") String from, @RequestParam("to") String to, HttpServletResponse response) throws IOException {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime fromDateTime = LocalDate.parse(from, formatter).atStartOfDay();
        LocalDateTime toDateTime = LocalDate.parse(to, formatter).atStartOfDay();

        JSONArray projectsJson = new JSONArray();
        List<Project> projects = projectService.findProjectsByTimePeriod(fromDateTime, toDateTime);
        for (Project project: projects) {
            JSONObject projectJson = new JSONObject();
            projectJson.put("name", project.getName());
            projectJson.put("description", project.getDescription());
            projectJson.put("creationDate", project.getCreationDate());
            projectJson.put("customer", project.getRelatedCustomer().getName());

            projectsJson.put(projectJson);
        }
        
        InputStreamResource resource = ZipUtils.createZipFromString(projectsJson.toString());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=projects_archive.zip");
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
