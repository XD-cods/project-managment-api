package com.vlad.projectservice.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse
            .builder()
            .error(String.valueOf(HttpStatus.NOT_FOUND.value()))
            .errorDescription(exception.getMessage())
            .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(BadRequestException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse
            .builder()
            .error(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .errorDescription(exception.getMessage())
            .build());
  }
}