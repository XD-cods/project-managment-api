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
    modelMapper.map(taskRequest, existTask);
//    if (taskRequest.getTaskStatus() != null) {
//      existTask.setTaskStatus(taskRequest.getTaskStatus());
//    }
//    if (taskRequest.getDeadline() != null) {
//      existTask.setDeadline(taskRequest.getDeadline());
//    }
//    if (taskRequest.getTitle() != null) {
//      existTask.setTitle(taskRequest.getTitle());
//    }
//    if (taskRequest.getDescription() != null) {
//      existTask.setDescription(taskRequest.getDescription());
//    }
  }
}
