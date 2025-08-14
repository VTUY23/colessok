package com.colessok.api.identity.dto.response;

import com.colessok.api.file.dto.response.FileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    FileResponse avatar;
    String firstName;
    String lastName;
    Boolean gender;
    String phone;
    String address;
    String dob;
}
