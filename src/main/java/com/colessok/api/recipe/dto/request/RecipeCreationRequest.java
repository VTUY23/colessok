package com.colessok.api.recipe.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeCreationRequest {
    String userId;
    String name;
    String tag;
    int time;
    String description;
    MultipartFile banner;
    List<StepCreationRequest> steps;
    List<IngredientCreationRequest> ingredients;
}
