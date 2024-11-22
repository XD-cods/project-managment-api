package com.vlad.projectservice.util.feign;

import com.vlad.projectservice.persistance.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/user")
public interface UserClient {
  @GetMapping("/{id}")
  ResponseEntity<User> getUserById(@PathVariable Long id);
}
