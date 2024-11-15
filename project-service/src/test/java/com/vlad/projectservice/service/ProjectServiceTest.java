package com.vlad.projectservice.service;

import com.vlad.projectservice.exception.ConflictException;
import com.vlad.projectservice.exception.NotFoundException;
import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.persistance.repository.ProjectRepository;
import com.vlad.projectservice.util.mapper.ProjectMapper;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
  @Mock
  private ProjectRepository projectRepository;

  @Spy
  private ProjectMapper projectMapper = new ProjectMapper(new ModelMapper());

  @InjectMocks
  private ProjectService projectService;

  @Test
  public void getProjectById_ShouldReturnProjectTest() {
    Project project = createTestProject();
    Long id = 1L;
    project.setId(id);

    when(projectRepository.findById(id)).thenReturn(Optional.of(project));

    ProjectResponse response = projectService.getProjectById(id);
    assertNotNull(response);
    assertEquals(project.getId(), response.getId());
  }

  @Test
  public void getProjectById_ShouldThrowNotFoundExceptionTest() {
    when(projectRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> projectService.getProjectById(0L));
  }

  @Test
  public void createProject_ShouldCreateProjectTest() {
    Project project = createTestProject();
    ProjectRequest projectRequest = createTestProjectRequest();

    when(projectRepository.save(any(Project.class))).thenReturn(project);

    ProjectResponse response = projectService.createProject(projectRequest);

    assertEquals(project.getId(), response.getId());
    assertEquals(project.getDescription(), response.getDescription());
    assertEquals(project.getName(), response.getName());
    assertEquals(project.getOwnerId(), response.getOwnerId());
  }

  @Test
  public void createProject_ShouldThrowConflictExceptionTest() {
    Project project = createTestProject();
    ProjectRequest projectRequest = createTestProjectRequest();

    when(projectRepository.findByName(projectRequest.getName())).thenReturn(Optional.of(project));

    assertThrows(ConflictException.class, () -> projectService.createProject(projectRequest));
  }

  @Test
  public void updateProject_ShouldUpdateProjectTest() {
    Project project = createTestProject();
    ProjectRequest projectRequest = createTestProjectRequest();
    projectRequest.setName("test name");

    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    ProjectResponse response = projectService.updateProject(project.getId(), projectRequest);

    assertEquals(project.getId(), response.getId());
    assertEquals(project.getDescription(), response.getDescription());
    assertEquals(project.getName(), projectRequest.getName());
  }

  @Test
  public void updateProject_ShouldThrowNotFoundExceptionTest() {
    when(projectRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> projectService.updateProject(0L, new ProjectRequest()));
  }

  @Test
  public void deleteProject_ShouldDeleteProjectTest() {
    Project project = createTestProject();

    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    Boolean response = projectService.deleteProject(project.getId());

    assertTrue(response);
  }

  @Test
  public void deleteProject_ShouldThrowNotFoundExceptionTest() {
    when(projectRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> projectService.deleteProject(0L));
  }

  private Project createTestProject() {
    return Project.builder()
            .id(1L)
            .description("test")
            .name("test")
            .ownerId(1L)
            .build();
  }

  private ProjectRequest createTestProjectRequest() {
    return ProjectRequest.builder()
            .description("test")
            .name("test")
            .ownerId(1L)
            .build();
  }
}