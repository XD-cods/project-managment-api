package com.vlad.userservice.service;

import com.vlad.userservice.exception.ConflictException;
import com.vlad.userservice.exception.NotFoundException;
import com.vlad.userservice.persistance.entity.Role;
import com.vlad.userservice.persistance.entity.User;
import com.vlad.userservice.persistance.repository.UserRepository;
import com.vlad.userservice.utility.mapper.UserMapper;
import com.vlad.userservice.web.request.UserRequest;
import com.vlad.userservice.web.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @Spy
  private UserMapper userMapper = new UserMapper(new ModelMapper());

  @InjectMocks
  private UserService userService;

  @Test
  public void getUserById_ShouldReturnUserTest() {
    User user = createTestUser();
    Long id = 1L;
    user.setId(id);

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    UserResponse response = userService.getUserById(id);
    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getEmail(), response.getEmail());
  }

  @Test
  public void getUserById_ShouldThrowNotFoundExceptionTest() {
    when(userRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getUserById(0L));
  }

  @Test
  public void createUser_ShouldCreateUserTest() {
    User user = createTestUser();
    UserRequest userRequest = createTestUserRequest();

    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.createUser(userRequest);

    assertNotEquals(user.getId(), response.getId());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getEmail(), response.getEmail());
    assertEquals(user.getRole(), response.getRole());
  }

  @Test
  public void createUser_ShouldThrowConflictByEmailExceptionTest() {
    User user = createTestUser();
    UserRequest userRequest = createTestUserRequest();

    when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

    assertThrows(ConflictException.class, () -> userService.createUser(userRequest));
  }


  @Test
  public void createUser_ShouldThrowConflictByUsernameExceptionTest() {
    User user = createTestUser();
    UserRequest userRequest = createTestUserRequest();

    when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(user));

    assertThrows(ConflictException.class, () -> userService.createUser(userRequest));
  }

  @Test
  public void updateUser_ShouldUpdateUserTest() {
    User user = createTestUser();
    UserRequest userRequest = createTestUserRequest();
    userRequest.setUsername("test name");

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    UserResponse response = userService.updateUser(user.getId(), userRequest);

    assertEquals(user.getId(), response.getId());
    assertEquals(user.getEmail(), response.getEmail());
    assertEquals(user.getUsername(), userRequest.getUsername());
  }

  @Test
  public void updateUser_ShouldThrowNotFoundExceptionTest() {
    when(userRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.updateUser(0L, new UserRequest()));
  }

  @Test
  public void deleteUser_ShouldDeleteUserTest() {
    User user = createTestUser();

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    Boolean response = userService.deleteUser(user.getId());

    assertTrue(response);
  }

  @Test
  public void deleteUser_ShouldThrowNotFoundExceptionTest() {
    when(userRepository.findById(0L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.deleteUser(0L));
  }

  private User createTestUser() {
    return User.builder()
            .id(1L)
            .username("test")
            .email("test@gmail.com")
            .role(Role.USER)
            .build();
  }

  private UserRequest createTestUserRequest() {
    return UserRequest.builder()
            .username("test")
            .email("test@gmail.com")
            .role(Role.USER)
            .build();
  }
}