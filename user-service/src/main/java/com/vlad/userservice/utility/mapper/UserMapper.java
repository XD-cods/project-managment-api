package com.vlad.userservice.utility.mapper;

import com.vlad.userservice.persistance.entity.User;
import com.vlad.userservice.web.request.UserRequest;
import com.vlad.userservice.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
  private final ModelMapper modelMapper;

  public UserResponse mapUserToUserResponse(User task) {
    return modelMapper.map(task, UserResponse.class);
  }

  public User mapUserRequestToUser(UserRequest taskRequest) {
    return modelMapper.map(taskRequest, User.class);
  }

  public void updateUserFromUserRequest(User existUser, UserRequest taskRequest) {
    modelMapper.map(existUser, taskRequest);
  }
}
