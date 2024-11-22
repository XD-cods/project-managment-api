package com.vlad.projectservice.service;

import com.vlad.projectservice.config.ApplicationConfig;
import com.vlad.projectservice.exception.ConflictException;
import com.vlad.projectservice.exception.NotFoundException;
import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.persistance.entity.Role;
import com.vlad.projectservice.persistance.entity.User;
import com.vlad.projectservice.persistance.repository.ProjectRepository;
import com.vlad.projectservice.util.feign.UserClient;
import com.vlad.projectservice.util.mapper.ProjectMapper;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(profiles = "test")
class ProjectServiceTest {

  //TODO: Profile load don't work
  private static final Logger logger = LoggerFactory.getLogger(ProjectServiceTest.class);

  @Mock
  private UserClient userClient;

  @Mock
  private ProjectRepository projectRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Spy
  private ProjectMapper projectMapper = new ProjectMapper(modelMapper);

  @InjectMocks
  private ProjectService projectService;

  @Test
  public void contextLoads() {
    logger.info("Application context loaded with profile: {}", System.getenv("spring.profiles.active"));
  }

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
    projectRequest.setTeamIds(List.of(1L, 2L, 3L));
    User user1 = new User(1L, "test1", "test1@mail.ru", Role.USER, List.of(project));
    User user2 = new User(2L, "test2", "test2@mail.ru", Role.USER, List.of(project));
    User user3 = new User(3L, "test3", "test3@mail.ru", Role.USER, List.of(project));

    when(userClient.getUserById(1L)).thenReturn(ResponseEntity.ok(user1));
    when(userClient.getUserById(2L)).thenReturn(ResponseEntity.ok(user2));
    when(userClient.getUserById(3L)).thenReturn(ResponseEntity.ok(user3));
    when(projectRepository.save(any(Project.class))).thenReturn(project);

    ProjectResponse response = projectService.createProject(projectRequest);

    assertEquals(project.getId(), response.getId());
    assertEquals(project.getDescription(), response.getDescription());
    assertEquals(project.getName(), response.getName());
    assertArrayEquals(projectRequest.getTeamIds().toArray(), response.getTeamIds().toArray());
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

    User user1 = new User(1L, "test1", "test1@mail.ru", Role.USER, List.of(project));
    User user2 = new User(2L, "test2", "test2@mail.ru", Role.USER, List.of(project));
    User user3 = new User(3L, "test3", "test3@mail.ru", Role.USER, List.of(project));

    when(userClient.getUserById(1L)).thenReturn(ResponseEntity.ok(user1));
    when(userClient.getUserById(2L)).thenReturn(ResponseEntity.ok(user2));
    when(userClient.getUserById(3L)).thenReturn(ResponseEntity.ok(user3));
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(userClient.getUserById(any(Long.class))).thenReturn(ResponseEntity.ok(new User()));

    ProjectResponse response = projectService.updateProject(project.getId(), projectRequest);

    assertEquals(project.getId(), response.getId());
    assertEquals(project.getDescription(), response.getDescription());
    assertEquals(project.getName(), projectRequest.getName());
    assertArrayEquals(projectRequest.getTeamIds().toArray(), response.getTeamIds().toArray());
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
            .owner(new User())
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