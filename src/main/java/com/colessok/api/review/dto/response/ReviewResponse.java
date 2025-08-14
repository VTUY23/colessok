package com.colessok.api.review.dto.response;

import java.util.List;

import com.colessok.api.file.dto.response.FileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String userId;
    String recipeId;
    int rating;
    String comment;
    List<FileResponse> media;
    int score;
    String createdAt;
    String updatedAt;
}
