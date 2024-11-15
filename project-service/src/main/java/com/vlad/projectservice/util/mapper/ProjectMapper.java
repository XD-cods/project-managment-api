package com.vlad.projectservice.util.mapper;

import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.web.request.ProjectRequest;
import com.vlad.projectservice.web.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {
  private final ModelMapper modelMapper;

  public ProjectResponse mapProjectToProjectResponse(Project project) {
    return modelMapper.map(project, ProjectResponse.class);
  }

  public void updateProjectFromProjectRequest(Project existProject, ProjectRequest projectRequest) {
    modelMapper.map(existProject, projectRequest);
  }

  public Project mapProjectRequestToProject(ProjectRequest projectRequest) {
    return modelMapper.map(projectRequest, Project.class);
  }
}
