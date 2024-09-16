package com.demo.DenuvoDemo.customerprojects.repos;

import com.demo.DenuvoDemo.customerprojects.entities.Project;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Andrii Filimonov
 */
public interface ProjectRepo extends JpaRepository<Project, Long> {
    
    Project findByName(String name);
    
    List<Project> findAllByCreationDateBetween(LocalDateTime from, LocalDateTime to);
    
}
