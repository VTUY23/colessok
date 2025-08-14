package com.colessok.api.recipe.dto.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeUpdateRequest {
    String name;
    String tag;
    MultipartFile banner;
    int time;
    String description;

    @Builder.Default
    List<StepUpdateRequest> steps = new ArrayList<>();

    @Builder.Default
    List<IngredientUpdateRequest> ingredients = new ArrayList<>();
}
