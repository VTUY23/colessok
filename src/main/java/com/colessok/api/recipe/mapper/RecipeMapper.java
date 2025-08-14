package com.colessok.api.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.colessok.api.file.mapper.FileMapper;
import com.colessok.api.identity.entity.User;
import com.colessok.api.recipe.dto.request.RecipeCreationRequest;
import com.colessok.api.recipe.dto.request.RecipeUpdateRequest;
import com.colessok.api.recipe.dto.response.RecipeResponse;
import com.colessok.api.recipe.entity.Recipe;

@Mapper(
        componentModel = "spring",
        uses = {FileMapper.class, IngredientMapper.class})
public interface RecipeMapper {
    @Mapping(target = "author", source = "userId")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Recipe toRecipe(RecipeCreationRequest request);

    @Mapping(target = "userId", source = "author.id")
    @Mapping(target = "categories", ignore = true)
    RecipeResponse toRecipeResponse(Recipe recipe);

    // Phương thức updateRecipe không có banner
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "banner", ignore = true)
    void updateRecipeNoBanner(@MappingTarget Recipe recipe, RecipeUpdateRequest request);

    void updateRecipe(@MappingTarget Recipe recipe, RecipeUpdateRequest request);

    // Cùng một phương thức chung cho việc loại bỏ các thuộc tính không cần thiết
    default void updateRecipeCommonLogic(RecipeUpdateRequest request, @MappingTarget Recipe recipe) {
        updateRecipeNoBanner(recipe, request); // Cập nhật nếu không có banner
        if (request.getBanner() != null) {
            updateRecipe(recipe, request); // Cập nhật nếu có banner
        }
    }

    default User map(String userId) {
        return userId != null ? User.builder().id(userId).build() : null;
    }

    default String map(User user) {
        return user != null ? user.getId() : null;
    }
}
