package com.colessok.api.recipe.dto.response;

import com.colessok.api.common.dto.ContentComparable;
import com.colessok.api.file.dto.response.FileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeStepResponse implements ContentComparable {
    String id;
    String title;
    String content;
    FileResponse media;
}
