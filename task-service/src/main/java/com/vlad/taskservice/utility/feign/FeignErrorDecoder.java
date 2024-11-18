package com.vlad.taskservice.utility.feign;

import com.vlad.taskservice.exception.ConflictException;
import com.vlad.taskservice.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    return switch (response.status()) {
      case 404 -> new NotFoundException("Resource not found");
      case 409 -> new ConflictException("Conflict occurred");
      default -> new RuntimeException("Unexpected error: " + response.reason());
    };
  }
}
