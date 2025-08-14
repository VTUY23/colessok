package com.colessok.api.identity.dto.request.profile;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.validator.dob.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {
    String firstName;
    String lastName;
    Boolean gender;
    String phone;
    String address;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    MultipartFile avatar;
}
