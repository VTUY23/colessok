package com.colessok.api.recipe.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.dto.ContentComparable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepUpdateRequest implements ContentComparable {
    String id;
    String title;
    String content;
    MultipartFile media;
}
