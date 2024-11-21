package com.vlad.taskservice;

import com.vlad.taskservice.exception.ConflictException;
import com.vlad.taskservice.exception.NotFoundException;
import com.vlad.taskservice.persistance.entity.Project;
import com.vlad.taskservice.persistance.entity.Task;
import com.vlad.taskservice.persistance.entity.User;
import com.vlad.taskservice.persistance.repository.TaskRepository;
import com.vlad.taskservice.service.TaskService;
import com.vlad.taskservice.utility.feign.UserClient;
import com.vlad.taskservice.utility.mapper.TaskMapper;
import com.vlad.taskservice.web.request.TaskRequest;
import com.vlad.taskservice.web.response.TaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceApplicationTests {

  @Mock
  private UserClient userClient;

  @Mock
  private TaskRepository taskRepository;

  @Spy
  private TaskMapper taskMapper = new TaskMapper(new ModelMapper());

  @InjectMocks
  private TaskService taskService;

  @Test
  public void getTaskById_ShouldReturnTaskTest() {
    Task task = createTestTask();
    Long id = 1L;
    task.setId(id);

    when(taskRepository.findById(id)).thenReturn(Optional.of(task));

    TaskResponse response = taskService.getTaskById(id);
    assertNotNull(response);
    assertEquals(task.getId(), response.getTaskId());
  }

  @Test
  public void getTaskById_ShouldThrowNotFoundExceptionTest() {
    when(taskRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.getTaskById(0L));
  }

  @Test
  public void createTask_ShouldCreateTaskTest() {
    Task task = createTestTask();
    TaskRequest taskRequest = createTestTaskRequest();

    when(userClient.getUserById(any(Long.class))).thenReturn(ResponseEntity.ok(new User()));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponse response = taskService.createTask(taskRequest);

    assertEquals(task.getId(), response.getTaskId());
    assertEquals(task.getDescription(), response.getDescription());
    assertEquals(task.getTitle(), response.getTitle());
  }

  @Test
  public void createTask_ShouldThrowConflictExceptionTest() {
    Task task = createTestTask();
    TaskRequest taskRequest = createTestTaskRequest();

    when(taskRepository.findByTitle(taskRequest.getTitle())).thenReturn(Optional.of(task));

    assertThrows(ConflictException.class, () -> taskService.createTask(taskRequest));
  }

  @Test
  public void updateTask_ShouldUpdateTaskTest() {
    Task task = createTestTask();
    TaskRequest taskRequest = createTestTaskRequest();
    taskRequest.setTitle("test name");

    when(userClient.getUserById(any(Long.class))).thenReturn(ResponseEntity.ok(new User()));
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

    TaskResponse response = taskService.updateTask(task.getId(), taskRequest);

    assertEquals(task.getId(), response.getTaskId());
    assertEquals(task.getDescription(), response.getDescription());
    assertEquals(task.getTitle(), taskRequest.getTitle());
  }

  @Test
  public void updateTask_ShouldThrowNotFoundExceptionTest() {
    when(taskRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.updateTask(0L, new TaskRequest()));
  }

  @Test
  public void deleteTask_ShouldDeleteTaskTest() {
    Task task = createTestTask();

    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    Boolean response = taskService.deleteTask(task.getId());

    assertTrue(response);
  }

  @Test
  public void deleteTask_ShouldThrowNotFoundExceptionTest() {
    when(taskRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.deleteTask(0L));
  }

  private Task createTestTask() {
    return Task.builder()
            .id(1L)
            .description("test")
            .title("test")
            .project(new Project())
            .build();
  }

  private TaskRequest createTestTaskRequest() {
    return TaskRequest.builder()
            .description("test")
            .title("test")
            .project(1L)
            .build();
  }
}
