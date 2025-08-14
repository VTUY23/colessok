package com.colessok.api.recipe.dto.response;

import com.colessok.api.common.dto.ContentComparable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientResponse implements ContentComparable {
    String id;
    String name;
    float quantity;
    String unit;
    String categoryId;
}
