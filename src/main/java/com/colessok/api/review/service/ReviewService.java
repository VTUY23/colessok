package com.colessok.api.review.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.common.exception.AppException;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.mapper.FileMapper;
import com.colessok.api.review.dto.request.ReviewCreationRequest;
import com.colessok.api.review.dto.request.ReviewUpdateRequest;
import com.colessok.api.review.dto.response.ReviewResponse;
import com.colessok.api.review.entity.Review;
import com.colessok.api.review.entity.ReviewFile;
import com.colessok.api.review.mapper.ReviewMapper;
import com.colessok.api.review.repository.ReviewFileRepository;
import com.colessok.api.review.repository.ReviewRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    FileMapper fileMapper;
    ReviewFileRepository reviewFileRepository;

    public ReviewResponse createReview(String recipeId, ReviewCreationRequest request) throws IOException {
        request.setUserId(getCurrentUserId());
        request.setRecipeId(recipeId);
        Review review = reviewMapper.toReview(request);
        Review savedReview = reviewRepository.save(review);
        Set<ReviewFile> mediaFiles = new HashSet<>();
        if (request.getMedia() != null && !request.getMedia().isEmpty()) {
            for (MultipartFile multipartFile : request.getMedia()) {
                File file = fileMapper.mapAndStoreFile(multipartFile);

                ReviewFile reviewFile = new ReviewFile();
                reviewFile.setFile(file);
                reviewFile.setReview(review);

                ReviewFile savedReviewFile = reviewFileRepository.save(reviewFile);
                mediaFiles.add(reviewFile);
            }
        }
        savedReview.getMedia().clear();
        savedReview.getMedia().addAll(mediaFiles);

        savedReview = reviewRepository.save(savedReview);
        return reviewMapper.toReviewResponse(savedReview, fileMapper);
    }

    private String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    @PreAuthorize("@reviewRepository.findById(#reviewId).get()" + ".author.id == authentication.name")
    public ReviewResponse updateReview(String reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new AppException(null));

        reviewMapper.updateReview(review, request);

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @PreAuthorize("@reviewRepository.findById(#reviewId).get()" + ".author.id == authentication.name")
    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public ReviewResponse getByReviewId(String reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new AppException(null));

        return reviewMapper.toReviewResponse(review);
    }

    public PageResponse<ReviewResponse> getByField(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Review> pageData;
        if (id == null) pageData = reviewRepository.findAllByAuthorId(getCurrentUserId(), pageable);
        else pageData = reviewRepository.findAllByRecipeId(id, pageable);

        var reviewList = pageData.stream()
                .map(review -> reviewMapper.toReviewResponse(review, fileMapper))
                .toList();

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(reviewList)
                .build();
    }
}
