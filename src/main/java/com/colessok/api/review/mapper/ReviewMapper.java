package com.colessok.api.review.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.colessok.api.file.dto.response.FileResponse;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.mapper.FileMapper;
import com.colessok.api.identity.entity.User;
import com.colessok.api.recipe.entity.Recipe;
import com.colessok.api.review.dto.request.ReviewCreationRequest;
import com.colessok.api.review.dto.request.ReviewUpdateRequest;
import com.colessok.api.review.dto.response.ReviewResponse;
import com.colessok.api.review.entity.Review;
import com.colessok.api.review.entity.ReviewFile;

@Mapper(
        componentModel = "spring",
        uses = {FileMapper.class})
public interface ReviewMapper {

    @Mapping(target = "author", source = "userId")
    @Mapping(target = "recipe", source = "recipeId")
    @Mapping(target = "media", ignore = true)
    Review toReview(ReviewCreationRequest request);

    @Mapping(target = "userId", source = "author")
    @Mapping(target = "recipeId", source = "recipe")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "userId", source = "author")
    @Mapping(target = "recipeId", source = "recipe")
    @Mapping(target = "media", expression = "java(getMediaFilesForReview(review, fileMapper))")
    ReviewResponse toReviewResponse(Review review, @Context FileMapper fileMapper);

    void updateReview(@MappingTarget Review review, ReviewUpdateRequest request);

    default List<FileResponse> mapMediaFiles(Set<ReviewFile> reviewFiles, @Context FileMapper fileMapper) {
        if (reviewFiles == null) {
            return Collections.emptyList();
        }

        return reviewFiles.stream()
                .map(ReviewFile::getFile)
                .filter(Objects::nonNull)
                .map(fileMapper::toFileResponse)
                .collect(Collectors.toList());
    }

    default List<FileResponse> getMediaFilesForReview(Review review, @Context FileMapper fileMapper) {
        Set<ReviewFile> reviewFiles = review.getMedia();
        List<FileResponse> mediaFileResponses = new ArrayList<>();

        if (reviewFiles != null && !reviewFiles.isEmpty()) {
            for (ReviewFile reviewFile : reviewFiles) {
                File file = reviewFile.getFile();
                if (file != null) {
                    FileResponse fileResponse = fileMapper.toFileResponse(file);
                    mediaFileResponses.add(fileResponse);
                }
            }
        }

        return mediaFileResponses;
    }

    FileResponse toFileResponse(File file);

    default User toUser(String userId) {
        return userId != null ? User.builder().id(userId).build() : null;
    }

    default String toUserId(User user) {
        return user != null ? user.getId() : null;
    }

    default Recipe toRecipe(String recipeId) {
        return recipeId != null ? Recipe.builder().id(recipeId).build() : null;
    }

    default String toRecipeId(Recipe recipe) {
        return recipe != null ? recipe.getId() : null;
    }
}
