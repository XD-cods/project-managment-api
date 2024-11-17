package com.vlad.taskservice.utility.feign;

import com.vlad.taskservice.persistance.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
@Component
public interface UserClient {
  @GetMapping("/api/user/{id}")
  ResponseEntity<User> getUserById(@PathVariable("id") Long id);
}
