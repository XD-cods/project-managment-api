package com.vlad.notifivationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  @KafkaListener(topics = "", groupId = "${spring.kafka.consumer.group-id}")
  public void consumeMessage(String message) {
    System.out.println("consume message: " + message);
  }
}
