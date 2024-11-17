package com.vlad.projectservice.service;

import com.vlad.projectservice.exception.ConflictException;
import com.vlad.projectservice.exception.NotFoundException;
import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.persistance.repository.ProjectRepository;
import com.vlad.projectservice.util.mapper.ProjectMapper;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;

  public static final String NOT_FOUND_MESSAGE = "Project not found by id: %d";
  public static final String CONFLICT_MESSAGE = "Project already exist by name: %s";

  public List<ProjectResponse> getAllProjects() {
    return projectRepository.findAll()
            .stream()
            .map(projectMapper::mapProjectToProjectResponse)
            .toList();
  }

  public ProjectResponse getProjectById(Long id) {
    Project existProject = projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    return projectMapper.mapProjectToProjectResponse(existProject);
  }

  public ProjectResponse createProject(ProjectRequest projectRequest) {
    if (projectRepository.findByName(projectRequest.getName()).isPresent()) {
      throw new ConflictException(String.format(CONFLICT_MESSAGE, projectRequest.getName()));
    }

    //TODO: Add team add and owner add logic
    Project newProject = projectMapper.mapProjectRequestToProject(projectRequest);
    projectRepository.save(newProject);
    return projectMapper.mapProjectToProjectResponse(newProject);
  }

  public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
    Project existProject = projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    //TODO: Add team add and owner add logic
    projectMapper.updateProjectFromProjectRequest(existProject, projectRequest);
    projectRepository.save(existProject);
    return projectMapper.mapProjectToProjectResponse(existProject);
  }

  public Boolean deleteProject(Long id) {
    projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    projectRepository.deleteById(id);
    return true;
  }
}
