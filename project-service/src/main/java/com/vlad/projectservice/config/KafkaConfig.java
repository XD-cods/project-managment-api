package com.vlad.projectservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic createTaskUpdatedTopic() {
    return new NewTopic("project-updated", 2, (short) 1);
  }

}
