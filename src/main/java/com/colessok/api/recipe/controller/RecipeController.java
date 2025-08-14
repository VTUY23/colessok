package com.colessok.api.recipe.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.dto.ApiResponse;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.recipe.dto.request.RecipeCreationRequest;
import com.colessok.api.recipe.dto.request.RecipeUpdateRequest;
import com.colessok.api.recipe.dto.response.RecipeResponse;
import com.colessok.api.recipe.service.RecipeService;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RecipeController {
    RecipeService recipeService;

    @PostMapping("/recipes")
    ApiResponse<RecipeResponse> createRecipe(
            @ModelAttribute RecipeCreationRequest request, @RequestParam(value = "banner") MultipartFile banner)
            throws IOException {
        return ApiResponse.<RecipeResponse>builder()
                .result(recipeService.createRecipe(request))
                .build();
    }

    @GetMapping("/recipes/{recipeId}")
    ApiResponse<RecipeResponse> getRecipe(@PathVariable("recipeId") String recipeId) {
        return ApiResponse.<RecipeResponse>builder()
                .result(recipeService.getByRecipeId(recipeId))
                .build();
    }

    @GetMapping("/users/{userId}/recipes")
    ApiResponse<PageResponse<RecipeResponse>> getUserRecipes(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<RecipeResponse>>builder()
                .result(recipeService.getByField(userId, page, size))
                .build();
    }

    @GetMapping("/recipes")
    ApiResponse<PageResponse<RecipeResponse>> getAllRecipes(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<RecipeResponse>>builder()
                .result(recipeService.getByField(null, page, size))
                .build();
    }

    @PutMapping("/recipes/{recipeId}")
    ApiResponse<RecipeResponse> updateRecipe(
            @PathVariable String recipeId,
            @ModelAttribute RecipeUpdateRequest request,
            @RequestParam(value = "banner", required = false) MultipartFile banner)
            throws IOException {
        return ApiResponse.<RecipeResponse>builder()
                .result(recipeService.updateRecipe(recipeId, request))
                .build();
    }

    @DeleteMapping("/recipes/{recipeId}")
    ApiResponse<String> deleteRecipe(@PathVariable String recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ApiResponse.<String>builder().result("Recipe has been deleted").build();
    }

    @GetMapping("/recipes/searchByName")
    public ApiResponse<PageResponse<RecipeResponse>> searchByName(
            @RequestParam String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<RecipeResponse>>builder()
                .result(recipeService.searchRecipesByName(name, page, size))
                .build();
    }
}
