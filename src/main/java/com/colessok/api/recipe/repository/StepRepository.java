package com.colessok.api.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.recipe.entity.RecipeStep;

@Repository
public interface StepRepository extends JpaRepository<RecipeStep, String> {}
