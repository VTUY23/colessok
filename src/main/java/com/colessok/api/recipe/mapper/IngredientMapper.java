package com.colessok.api.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.colessok.api.recipe.dto.request.IngredientCreationRequest;
import com.colessok.api.recipe.dto.request.IngredientUpdateRequest;
import com.colessok.api.recipe.dto.response.IngredientResponse;
import com.colessok.api.recipe.entity.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    Ingredient toIngredient(IngredientCreationRequest request);

    @Mapping(target = "recipeId", ignore = true)
    IngredientCreationRequest toIngredient(IngredientUpdateRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    IngredientResponse toIngredientResponse(Ingredient ingredient);

    @Mapping(target = "category", ignore = true)
    void update(@MappingTarget Ingredient ingredient, IngredientUpdateRequest request);
}
