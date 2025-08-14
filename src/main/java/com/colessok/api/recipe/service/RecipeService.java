package com.colessok.api.recipe.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.dto.ContentComparable;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.common.exception.AppException;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.repository.FileMgmtRepository;
import com.colessok.api.recipe.dto.request.IngredientCreationRequest;
import com.colessok.api.recipe.dto.request.IngredientUpdateRequest;
import com.colessok.api.recipe.dto.request.RecipeCreationRequest;
import com.colessok.api.recipe.dto.request.RecipeUpdateRequest;
import com.colessok.api.recipe.dto.request.StepCreationRequest;
import com.colessok.api.recipe.dto.response.RecipeResponse;
import com.colessok.api.recipe.entity.Ingredient;
import com.colessok.api.recipe.entity.Recipe;
import com.colessok.api.recipe.entity.RecipeStep;
import com.colessok.api.recipe.mapper.IngredientMapper;
import com.colessok.api.recipe.mapper.RecipeMapper;
import com.colessok.api.recipe.mapper.RecipeStepMapper;
import com.colessok.api.recipe.repository.RecipeRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RecipeService {
    RecipeRepository recipeRepository;
    RecipeMapper recipeMapper;
    RecipeStepMapper recipeStepMapper;
    IngredientMapper ingredientMapper;
    FileMgmtRepository fileMgmtRepository;
    StepService stepService;
    IngredientService ingredientService;

    @Transactional
    public RecipeResponse createRecipe(RecipeCreationRequest request) throws IOException {
        request.setUserId(getCurrentUserId());

        if (recipeRepository.existsByName(request.getName())) {
            throw new AppException(null);
        }

        var recipe = recipeMapper.toRecipe(request);

        recipe = recipeRepository.save(recipe);
        Set<RecipeStep> steps = new HashSet<>();
        if (request.getSteps() != null) {
            for (StepCreationRequest stepRequest : request.getSteps()) {
                stepRequest.setRecipeId(recipe.getId());
                var step = stepService.createStep(stepRequest);
                steps.add(step);
            }
        }
        recipe.setSteps(steps);
        Set<Ingredient> ingredients = new HashSet<>();
        if (request.getIngredients() != null) {
            for (IngredientCreationRequest ingredientRequest : request.getIngredients()) {
                ingredientRequest.setRecipeId(recipe.getId());
                var ingredient = ingredientService.create(ingredientRequest);
                ingredients.add(ingredient);
            }
        }
        recipe.setIngredients(ingredients);
        return recipeMapper.toRecipeResponse(recipe);
    }

    private String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    public RecipeResponse getByRecipeId(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(null));

        return recipeMapper.toRecipeResponse(recipe);
    }

    public PageResponse<RecipeResponse> getByField(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Recipe> pageData;
        if (id == null) pageData = recipeRepository.findAll(pageable);
        else pageData = recipeRepository.findAllByAuthorId(id, pageable);

        var recipeList = pageData.stream().map(recipeMapper::toRecipeResponse).toList();

        return PageResponse.<RecipeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(recipeList)
                .build();
    }

    @PreAuthorize("@recipeRepository.findById(#recipeId).get()" + ".author.id == authentication.name")
    public RecipeResponse updateRecipe(String recipeId, RecipeUpdateRequest request) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(null));

        // if (isChanged(recipe.getSteps(), request.getSteps(),
        // recipeStepMapper::toRecipeStepResponse)
        // || isChanged(recipe.getIngredients(), request.getIngredients(),
        // ingredientMapper::toIngredientResponse))
        File oldMedia = recipe.getBanner();
        MultipartFile mediaFile = request.getBanner();
        if (mediaFile != null && !mediaFile.isEmpty()) {
            // // Xóa avatar cũ nếu có
            if (oldMedia != null) fileMgmtRepository.delete(oldMedia);
        }

        Set<RecipeStep> stepsOld = recipe.getSteps();
        Set<RecipeStep> steps = new HashSet<>();
        if (request.getSteps() != null) {
            for (var stepRequest : request.getSteps()) {
                RecipeStep step;
                if (stepRequest.getId() == null) {
                    var create = recipeStepMapper.toRecipeStep(stepRequest);
                    create.setRecipeId(recipe.getId());
                    step = stepService.createStep(create);
                } else step = stepService.update(stepRequest);
                steps.add(step);
            }
        }
        stepsOld.clear();
        stepsOld.addAll(steps);
        Set<Ingredient> ingredientsOld = recipe.getIngredients();
        Set<Ingredient> ingredients = new HashSet<>();
        if (request.getIngredients() != null) {
            for (IngredientUpdateRequest ingredientRequest : request.getIngredients()) {
                // Gọi StepService để tạo từng bước
                Ingredient ingredient;
                if (ingredientRequest.getId() == null) {
                    var create = ingredientMapper.toIngredient(ingredientRequest);
                    create.setRecipeId(recipe.getId());
                    ingredient = ingredientService.create(create);
                } else ingredient = ingredientService.update(ingredientRequest);
                ingredients.add(ingredient);
            }
        }
        ingredientsOld.clear();
        ingredientsOld.addAll(ingredients);
        recipe.setUpdatedAt(LocalDateTime.now());
        if (request.getBanner() != null && !request.getBanner().isEmpty()) recipeMapper.updateRecipe(recipe, request);
        else recipeMapper.updateRecipeNoBanner(recipe, request);

        return recipeMapper.toRecipeResponse(recipeRepository.save(recipe));
    }

    private <T, R extends ContentComparable> boolean isChanged(
            Set<T> oldDatas, List<R> newDatas, Function<T, R> toResponseFn) {
        if (newDatas.stream().anyMatch(s -> s.getId() == null) || oldDatas.size() != newDatas.size()) return true;

        Map<String, R> oldMap = oldDatas.stream()
                .map(toResponseFn)
                .collect(Collectors.toMap(ContentComparable::getId, Function.identity()));

        for (var newData : newDatas) {
            var oldStep = oldMap.get(newData.getId());
            if (oldStep == null || !oldStep.toString().equals(newData.toString())) return true;
        }
        return false;
    }

    @PreAuthorize(
            "@recipeRepository.findById(#recipeId).get()" + ".author.id == authentication.name or hasRole('ADMIN')")
    public void deleteRecipe(String recipeId) {
        // recipeRepository.deleteById(recipeId);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(null));

        recipe.setDeleted(true);
        recipe.setUpdatedAt(LocalDateTime.now());
        recipeRepository.save(recipe);
    }

    public PageResponse<RecipeResponse> searchRecipesByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Recipe> pageData;
        pageData = recipeRepository.findByNameContainingIgnoreCase(name, pageable);

        var recipeList = pageData.stream().map(recipeMapper::toRecipeResponse).toList();

        return PageResponse.<RecipeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(recipeList)
                .build();
    }
}
