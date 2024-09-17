package com.demo.DenuvoDemo;

import com.demo.DenuvoDemo.customerprojects.entities.ErrorHandler;
import com.demo.DenuvoDemo.services.CustomerService;
import com.demo.DenuvoDemo.services.ProjectService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DenuvoDemoApplication {
    
    @Bean
    public CustomerService customerService() {
        return new CustomerService();
    }
    
    @Bean
    public ProjectService projectService() {
        return new ProjectService();
    }
    
    @Bean
    public ErrorHandler errrHandler() {
        return new ErrorHandler();
    }
    public static void main(String[] args) {
            SpringApplication.run(DenuvoDemoApplication.class, args);
    }

}
