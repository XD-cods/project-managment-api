package com.vlad.projectservice.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.projectservice.persistance.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

  private String name;

  private String description;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("team_ids")
  private List<Long> teamIds;

  private ProjectStatus projectStatus;

}
