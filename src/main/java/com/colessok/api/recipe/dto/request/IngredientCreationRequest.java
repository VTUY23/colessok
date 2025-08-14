package com.colessok.api.recipe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientCreationRequest {
    String recipeId;
    String name;
    float quantity;
    String unit;
    String categoryId;
}
