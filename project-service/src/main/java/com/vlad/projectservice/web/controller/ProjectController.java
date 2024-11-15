package com.vlad.projectservice.web.controller;

import com.vlad.projectservice.service.ProjectService;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping
  public ResponseEntity<List<ProjectResponse>> getAllProjects() {
    return ResponseEntity.ok(projectService.getAllProjects());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
    return ResponseEntity.ok(projectService.getProjectById(id));
  }

  @PostMapping
  public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
    return ResponseEntity.ok(projectService.createProject(projectRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) {
    return ResponseEntity.ok(projectService.updateProject(id, projectRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deleteProject(@PathVariable Long id) {
    return ResponseEntity.ok(projectService.deleteProject(id));
  }
}
