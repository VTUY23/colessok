package com.colessok.api.category.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryTreeResponse {
    String id;
    String name;
    Boolean type;
    List<CategoryTreeResponse> children;
}
