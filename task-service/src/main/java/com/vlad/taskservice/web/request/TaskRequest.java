package com.vlad.taskservice.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.taskservice.persistance.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
  private String title;

  private String description;

  @JsonProperty("project_id")
  private Long projectId;

  @JsonProperty("assignee_id")
  private Long assigneeId;

  @JsonProperty("project_status")
  private TaskStatus projectStatus;

  private LocalDateTime deadline;
}
