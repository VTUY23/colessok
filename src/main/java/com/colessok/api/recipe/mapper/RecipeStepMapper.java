package com.colessok.api.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.colessok.api.recipe.dto.request.StepCreationRequest;
import com.colessok.api.recipe.dto.request.StepUpdateRequest;
import com.colessok.api.recipe.dto.response.RecipeStepResponse;
import com.colessok.api.recipe.entity.RecipeStep;

@Mapper(componentModel = "spring")
public interface RecipeStepMapper {
    @Mapping(target = "recipe", ignore = true)
    RecipeStep toRecipeStep(StepCreationRequest step);

    @Mapping(target = "recipeId", ignore = true)
    StepCreationRequest toRecipeStep(StepUpdateRequest step);

    RecipeStepResponse toRecipeStepResponse(RecipeStep recipeStep);

    void update(@MappingTarget RecipeStep recipeStep, StepUpdateRequest request);
}
