package com.vlad.taskservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic createTaskUpdatedTopic() {
    return new NewTopic("task-updated", 2, (short) 1);
  }

}
