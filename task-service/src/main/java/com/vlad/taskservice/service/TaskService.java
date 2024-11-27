package com.vlad.taskservice.service;

import com.vlad.taskservice.exception.ConflictException;
import com.vlad.taskservice.exception.NotFoundException;
import com.vlad.taskservice.persistance.entity.Project;
import com.vlad.taskservice.persistance.entity.Task;
import com.vlad.taskservice.persistance.entity.User;
import com.vlad.taskservice.persistance.repository.TaskRepository;
import com.vlad.taskservice.utility.feign.ProjectClient;
import com.vlad.taskservice.utility.feign.UserClient;
import com.vlad.taskservice.utility.kafka.TaskKafkaProducer;
import com.vlad.taskservice.utility.mapper.TaskMapper;
import com.vlad.taskservice.web.request.TaskRequest;
import com.vlad.taskservice.web.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final ProjectClient projectClient;
  private final UserClient userClient;
  private final TaskKafkaProducer taskKafkaProducer;

  public static final String NOT_FOUND_MESSAGE = "Task not found by id: %d";
  public static final String CONFLICT_MESSAGE = "Task already exist by name: %s";

  public List<TaskResponse> getAllTasks() {
    return taskRepository.findAll()
            .stream()
            .map(taskMapper::mapTaskToTaskResponse)
            .toList();
  }

  public TaskResponse getTaskById(Long id) {
    Task existTask = taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    return taskMapper.mapTaskToTaskResponse(existTask);
  }

  public TaskResponse createTask(TaskRequest taskRequest) {
    if (taskRepository.findByTitle(taskRequest.getTitle()).isPresent()) {
      throw new ConflictException(String.format(CONFLICT_MESSAGE, taskRequest.getTitle()));
    }

    Task newTask = taskMapper.mapTaskRequestToTask(taskRequest);

    if (taskRequest.getAssignee() != null) {
      newTask.setAssignee(getUserById(taskRequest.getAssignee()));
    }
    if (taskRequest.getProject() != null) {
      newTask.setProject(getProjectById(taskRequest.getProject()));
    }

    taskRepository.save(newTask);
    return taskMapper.mapTaskToTaskResponse(newTask);
  }

  public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
    Task existTask = taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    if (taskRequest.getAssignee() != null) {
      existTask.setAssignee(getUserById(taskRequest.getAssignee()));
    }
    if (taskRequest.getProject() != null) {
      existTask.setProject(getProjectById(taskRequest.getProject()));
    }

    taskMapper.updateTaskFromTaskRequest(existTask, taskRequest);
    taskRepository.save(existTask);
    taskKafkaProducer.sendTaskUpdatedToKafka(existTask.getTitle());
    return taskMapper.mapTaskToTaskResponse(existTask);
  }

  public Boolean deleteTask(Long id) {
    taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    taskRepository.deleteById(id);
    return true;
  }

  private User getUserById(Long id) {
    return userClient.getUserById(id).getBody();
  }

  private Project getProjectById(Long id) {
    return projectClient.getProjectById(id).getBody();
  }
}
