package com.vlad.notifivationservice.util.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "project-updated")
  public void consumeMessage(String message) {
    System.out.println("consume message: " + message);
  }

  @KafkaListener(topics = "task-updated")
  public void consumeMessageFromTask(String message) {
    System.out.println("consume message task: " + message);
  }
}
