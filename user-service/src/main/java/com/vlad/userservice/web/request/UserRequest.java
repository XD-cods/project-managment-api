package com.vlad.userservice.web.request;

import com.vlad.userservice.persistance.entity.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

  private String username;

  @Email
  private String email;

  private Role role;

}
