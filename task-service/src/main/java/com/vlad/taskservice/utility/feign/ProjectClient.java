package com.vlad.taskservice.utility.feign;

import com.vlad.taskservice.persistance.entity.Project;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
@Component
public interface ProjectClient {
  @GetMapping("/api/project/{id}")
  ResponseEntity<Project> getProjectById(@PathVariable("id") Long id);
}
