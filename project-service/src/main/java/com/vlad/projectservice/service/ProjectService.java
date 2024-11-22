package com.vlad.projectservice.service;

import com.vlad.projectservice.exception.ConflictException;
import com.vlad.projectservice.exception.NotFoundException;
import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.persistance.entity.User;
import com.vlad.projectservice.persistance.repository.ProjectRepository;
import com.vlad.projectservice.util.feign.UserClient;
import com.vlad.projectservice.util.mapper.ProjectMapper;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;
  private final UserClient userClient;

  public static final String NOT_FOUND_MESSAGE = "Project not found by id: %d";
  public static final String CONFLICT_MESSAGE = "Project already exist by name: %s";

  @Transactional
  public List<ProjectResponse> getAllProjects() {
    return projectRepository.findAll()
            .stream()
            .map(projectMapper::mapProjectToProjectResponse)
            .toList();
  }

  @Transactional
  public ProjectResponse getProjectById(Long id) {
    Project existProject = projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    return projectMapper.mapProjectToProjectResponse(existProject);
  }

  @Transactional
  public ProjectResponse createProject(ProjectRequest projectRequest) {
    if (projectRepository.findByName(projectRequest.getName()).isPresent()) {
      throw new ConflictException(String.format(CONFLICT_MESSAGE, projectRequest.getName()));
    }

    Project newProject = projectMapper.mapProjectRequestToProject(projectRequest);

    if (projectRequest.getTeamIds() != null && !projectRequest.getTeamIds().isEmpty()) {
      List<User> users = projectRequest.getTeamIds()
              .stream()
              .map(this::getUserById)
              .toList();


      newProject.getTeamMembers().clear();
      newProject.getTeamMembers().addAll(users);
    }

    if (projectRequest.getOwnerId() != null) {
      newProject.setOwner(getUserById(projectRequest.getOwnerId()));
    }

    projectRepository.save(newProject);
    return projectMapper.mapProjectToProjectResponse(newProject);
  }

  @Transactional
  public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
    Project existProject = projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    if (projectRequest.getTeamIds() != null) {
      existProject.getTeamMembers().clear();
    }

    if (projectRequest.getTeamIds() != null && !projectRequest.getTeamIds().isEmpty()) {
      List<User> users = projectRequest.getTeamIds()
              .stream()
              .map(this::getUserById)
              .toList();
      users.stream().map(user -> user.getProjects().add(existProject));

      existProject.getTeamMembers().clear();
      existProject.getTeamMembers().addAll(users);
    }

    if (projectRequest.getOwnerId() != null) {
      existProject.setOwner(getUserById(projectRequest.getOwnerId()));
    }

    projectMapper.updateProjectFromProjectRequest(existProject, projectRequest);
    projectRepository.save(existProject);
    return projectMapper.mapProjectToProjectResponse(existProject);
  }

  @Transactional
  public Boolean deleteProject(Long id) {
    projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    projectRepository.deleteById(id);
    return true;
  }

  private User getUserById(Long id) {
    return userClient.getUserById(id).getBody();
  }


}
