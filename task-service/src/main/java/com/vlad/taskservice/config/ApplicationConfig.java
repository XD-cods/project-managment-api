package com.vlad.taskservice.config;

import com.vlad.taskservice.persistance.entity.Task;
import com.vlad.taskservice.web.response.TaskResponse;
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

    modelMapper.createTypeMap(Task.class, TaskResponse.class)
            .addMappings(m -> {
              m.skip(TaskResponse::setAssignee);
              m.skip(TaskResponse::setProject);
            }).setPostConverter(toTaskResponseConverter());


    return modelMapper;
  }

  public Converter<Task, TaskResponse> toTaskResponseConverter() {
    return context -> {
      Task task = context.getSource();
      TaskResponse taskResponse = context.getDestination();
      mapSpecificFields(task, taskResponse);
      return context.getDestination();
    };
  }

  private void mapSpecificFields(Task source, TaskResponse destination) {
    destination.setAssignee(source.getAssignee() != null ? source.getAssignee().getId() : null);
    destination.setProject(source.getProject() != null ? source.getProject().getId() : null);
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
