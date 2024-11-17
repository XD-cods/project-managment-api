package com.vlad.userservice.service;

import com.vlad.userservice.exception.ConflictException;
import com.vlad.userservice.exception.NotFoundException;
import com.vlad.userservice.persistance.entity.User;
import com.vlad.userservice.persistance.repository.UserRepository;
import com.vlad.userservice.utility.mapper.UserMapper;
import com.vlad.userservice.web.request.UserRequest;
import com.vlad.userservice.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public static final String NOT_FOUND_MESSAGE = "User not found by id: %d";
  public static final String CONFLICT_EMAIL_MESSAGE = "User already exist by email: %s";
  public static final String CONFLICT_USERNAME_MESSAGE = "User already exist by username: %s";

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
            .stream()
            .map(userMapper::mapUserToUserResponse)
            .toList();
  }

  public UserResponse getUserById(Long id) {
    User existUser = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    return userMapper.mapUserToUserResponse(existUser);
  }

  public UserResponse createUser(UserRequest userRequest) {
    if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
      throw new ConflictException(String.format(CONFLICT_EMAIL_MESSAGE, userRequest.getEmail()));
    }

    if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
      throw new ConflictException(String.format(CONFLICT_USERNAME_MESSAGE, userRequest.getUsername()));
    }

    User newUser = userMapper.mapUserRequestToUser(userRequest);
    userRepository.save(newUser);
    return userMapper.mapUserToUserResponse(newUser);
  }

  public UserResponse updateUser(Long id, UserRequest userRequest) {
    User existUser = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    userMapper.updateUserFromUserRequest(existUser, userRequest);
    userRepository.save(existUser);
    return userMapper.mapUserToUserResponse(existUser);
  }

  public Boolean deleteUser(Long id) {
    userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    userRepository.deleteById(id);
    return true;
  }
}
