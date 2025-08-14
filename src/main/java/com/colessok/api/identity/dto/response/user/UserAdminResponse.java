package com.colessok.api.identity.dto.response.user;

import java.util.Set;

import com.colessok.api.identity.dto.response.RoleResponse;
import com.colessok.api.identity.dto.response.UserProfileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAdminResponse {
    String id;
    String username;
    String email;
    boolean emailVerified;
    Set<RoleResponse> roles;
    UserProfileResponse profile;
    String createdAt;
    String updatedAt;
    String lastLogin;
    boolean isDeleted;
}
