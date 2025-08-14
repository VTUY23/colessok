package com.colessok.api.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    Page<Review> findAllByRecipeId(String recipeId, Pageable pageable);

    Page<Review> findAllByAuthorId(String userId, Pageable pageable);
}
