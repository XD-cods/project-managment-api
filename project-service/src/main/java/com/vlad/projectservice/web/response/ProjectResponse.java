package com.vlad.projectservice.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.projectservice.persistance.entity.Status;
import com.vlad.projectservice.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
  private Long id;

  private String name;

  private String description;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("team_ids")
  private List<User> teamIds;

  private Status status;
}
