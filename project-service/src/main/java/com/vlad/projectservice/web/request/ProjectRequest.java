package com.vlad.projectservice.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.projectservice.persistance.entity.Status;
import jakarta.validation.constraints.NotBlank;
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

  @NotBlank
  private String name;

  private String description;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("team_ids")
  private List<Long> teamIds;

  private Status status;

}
