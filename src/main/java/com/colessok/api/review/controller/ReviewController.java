package com.colessok.api.review.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colessok.api.common.dto.ApiResponse;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.review.dto.request.ReviewCreationRequest;
import com.colessok.api.review.dto.request.ReviewUpdateRequest;
import com.colessok.api.review.dto.response.ReviewResponse;
import com.colessok.api.review.service.ReviewService;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewController {
    ReviewService reviewService;

    @GetMapping("/recipes/{recipeId}/reviews")
    ApiResponse<PageResponse<ReviewResponse>> getByRecipes(
            @PathVariable String recipeId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .result(reviewService.getByField(recipeId, page, size))
                .build();
    }

    @GetMapping("/users/reviews")
    ApiResponse<PageResponse<ReviewResponse>> getMyReviews(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .result(reviewService.getByField(null, page, size))
                .build();
    }

    @GetMapping("/reviews/{reviewId}")
    ApiResponse<ReviewResponse> getReview(@PathVariable String reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.getByReviewId(reviewId))
                .build();
    }

    @PostMapping("/recipes/{recipeId}/reviews")
    ApiResponse<ReviewResponse> createReview(
            @PathVariable String recipeId, @ModelAttribute ReviewCreationRequest request) throws IOException {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(recipeId, request))
                .build();
    }

    @PutMapping("/reviews/{reviewId}")
    ApiResponse<ReviewResponse> updateReview(@PathVariable String reviewId, @RequestBody ReviewUpdateRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReview(reviewId, request))
                .build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    ApiResponse<String> deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<String>builder().result("Review has been deleted").build();
    }
}
