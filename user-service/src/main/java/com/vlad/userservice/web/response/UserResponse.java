package com.vlad.userservice.web.response;

import com.vlad.userservice.persistance.entity.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private Long id;

  private String username;

  @Email
  private String email;

  private Role role;
}
