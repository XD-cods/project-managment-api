package com.vlad.projectservice.web.request;

import com.vlad.projectservice.persistance.entity.Role;
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
