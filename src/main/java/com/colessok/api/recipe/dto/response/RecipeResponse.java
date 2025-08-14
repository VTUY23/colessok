package com.colessok.api.recipe.dto.response;

import java.util.List;

import com.colessok.api.category.dto.response.CategoryResponse;
import com.colessok.api.file.dto.response.FileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeResponse {
    String id;
    String userId;
    String name;
    String tag;
    int time;
    String description;
    float rating;
    int reviewCount;
    FileResponse banner;
    List<RecipeStepResponse> steps;
    List<IngredientResponse> ingredients;
    List<CategoryResponse> categories;
    String createdAt;
    String updatedAt;
}
