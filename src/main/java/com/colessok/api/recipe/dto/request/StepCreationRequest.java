package com.colessok.api.recipe.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepCreationRequest {
    String recipeId;
    String title;
    String content;
    MultipartFile media;
}
