package com.vlad.projectservice.util.feign;

import com.vlad.projectservice.exception.ConflictException;
import com.vlad.projectservice.exception.NotFoundException;
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
