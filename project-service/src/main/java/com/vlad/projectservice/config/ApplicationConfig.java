package com.vlad.projectservice.config;

import com.vlad.projectservice.persistance.entity.Project;
import com.vlad.projectservice.persistance.entity.User;
import com.vlad.projectservice.web.response.ProjectResponse;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public ModelMapper modelMapper() {

    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

    modelMapper.createTypeMap(Project.class, ProjectResponse.class)
            .addMappings(m -> {
              m.skip(ProjectResponse::setTeamIds);
              m.skip(ProjectResponse::setOwnerId);
            }).setPostConverter(toProjectResponseConverter());

    return modelMapper;

  }

  public Converter<Project, ProjectResponse> toProjectResponseConverter() {
    return context -> {
      Project project = context.getSource();
      ProjectResponse projectResponse = context.getDestination();
      mapSpecificFields(project, projectResponse);
      return context.getDestination();
    };
  }

  private void mapSpecificFields(Project source, ProjectResponse destination) {
    destination.setTeamIds(source.getTeamMembers().stream().map(User::getId).toList());
    destination.setOwnerId(source.getOwner() != null ? source.getOwner().getId() : null);
  }

  //  @Bean
//  public OpenAPI userOpenAPI(
//          @Value("${openapi.service.title}") String serviceTitle,
//          @Value("${openapi.service.version}") String serviceVersion,
//          @Value("${openapi.service.url}") String url) {
//    return new OpenAPI()
//            .servers(List.of(new Server().url(url)))
//            .info(new Info().title(serviceTitle).version(serviceVersion));
//  }
}
