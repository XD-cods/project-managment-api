package com.vlad.taskservice.service;

import com.vlad.taskservice.exception.ConflictException;
import com.vlad.taskservice.exception.NotFoundException;
import com.vlad.taskservice.persistance.entity.Task;
import com.vlad.taskservice.persistance.repository.TaskRepository;
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

    //TODO: Add project add and assignee logic
    Task newTask = taskMapper.mapTaskRequestToTask(taskRequest);
    taskRepository.save(newTask);
    return taskMapper.mapTaskToTaskResponse(newTask);
  }

  public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
    Task existTask = taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    //TODO: Add project add and assignee logic
    taskMapper.updateTaskFromTaskRequest(existTask, taskRequest);
    taskRepository.save(existTask);
    return taskMapper.mapTaskToTaskResponse(existTask);
  }

  public Boolean deleteTask(Long id) {
    taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    taskRepository.deleteById(id);
    return true;
  }
}
