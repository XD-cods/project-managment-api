package com.vlad.projectservice.util.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectKafkaProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendProjectUpdatedToKafka(String projectName) {
    kafkaTemplate.send("project-updated", projectName);
  }
}
