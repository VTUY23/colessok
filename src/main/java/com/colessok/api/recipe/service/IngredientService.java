package com.colessok.api.recipe.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.colessok.api.category.entity.Category;
import com.colessok.api.category.repository.CategoryRepository;
import com.colessok.api.common.exception.AppException;
import com.colessok.api.recipe.dto.request.IngredientCreationRequest;
import com.colessok.api.recipe.dto.request.IngredientUpdateRequest;
import com.colessok.api.recipe.entity.Ingredient;
import com.colessok.api.recipe.entity.Recipe;
import com.colessok.api.recipe.mapper.IngredientMapper;
import com.colessok.api.recipe.repository.IngredientRepository;
import com.colessok.api.recipe.repository.RecipeRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IngredientService {
    IngredientRepository ingredientRepository;
    IngredientMapper ingredientMapper;
    CategoryRepository categoryRepository;
    RecipeRepository recipeRepository;

    @Transactional
    public Ingredient create(IngredientCreationRequest request) {
        var ingredient = ingredientMapper.toIngredient(request);
        if (request.getRecipeId() != null) {
            Recipe recipe = recipeRepository.findById(request.getRecipeId()).orElseThrow(() -> new AppException(null));
            ingredient.setRecipe(recipe);
        }
        if (request.getCategoryId() != null) {
            Category category =
                    categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(null));
            ingredient.setCategory(category);
            ingredient.setName(category.getName());
        }
        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }

    @Transactional
    public Ingredient update(IngredientUpdateRequest request) {
        var ingredient = ingredientRepository.findById(request.getId()).orElseThrow(() -> new AppException(null));
        ingredientMapper.update(ingredient, request);
        if (request.getCategoryId() != null) {
            Category category =
                    categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(null));
            ingredient.setCategory(category);
            ingredient.setName(category.getName());
        }
        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }
}
