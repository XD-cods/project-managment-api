package com.vlad.taskservice.utility.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskKafkaProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendTaskUpdatedToKafka(String taskName) {
    kafkaTemplate.send("task-updated", taskName);
  }

}
