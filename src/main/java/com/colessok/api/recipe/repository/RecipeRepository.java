package com.colessok.api.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.recipe.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, String> {

    Page<Recipe> findAllByAuthorId(String userId, Pageable pageable);

    boolean existsByName(String name);

    Page<Recipe> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
