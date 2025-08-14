package com.colessok.api.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.review.entity.ReviewFile;

@Repository
public interface ReviewFileRepository extends JpaRepository<ReviewFile, String> {
    List<ReviewFile> findAllByReview_Id(String reviewId);
}
