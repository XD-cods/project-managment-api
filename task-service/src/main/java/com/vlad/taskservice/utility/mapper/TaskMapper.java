package com.vlad.taskservice.utility.mapper;

import com.vlad.taskservice.persistance.entity.Task;
import com.vlad.taskservice.web.request.TaskRequest;
import com.vlad.taskservice.web.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {
  private final ModelMapper modelMapper;

  public TaskResponse mapTaskToTaskResponse(Task task) {
    return modelMapper.map(task, TaskResponse.class);
  }

  public Task mapTaskRequestToTask(TaskRequest taskRequest) {
    return modelMapper.map(taskRequest, Task.class);
  }

  public void updateTaskFromTaskRequest(Task existTask, TaskRequest taskRequest) {
    modelMapper.map(existTask, taskRequest);
  }
}
