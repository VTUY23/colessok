package com.colessok.api.identity.dto.request.user;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 6, message = "INVALID_PASSWORD")
    String currentPassword;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String newPassword;
}
