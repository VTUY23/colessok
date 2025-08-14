package com.colessok.api.identity.dto.response.user;

import com.colessok.api.identity.dto.response.UserProfileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSelfResponse {
    String id;
    String username;
    String email;
    boolean emailVerified;
    UserProfileResponse profile;
}
