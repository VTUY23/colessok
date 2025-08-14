package com.colessok.api.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.colessok.api.category.dto.request.CategoryRequest;
import com.colessok.api.category.dto.response.CategoryResponse;
import com.colessok.api.category.dto.response.CategoryTreeResponse;
import com.colessok.api.category.entity.Category;
import com.colessok.api.category.repository.CategoryRepository;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    protected CategoryRepository categoryRepository;

    @Mapping(target = "parent", ignore = true)
    public abstract Category toCategory(CategoryRequest request);

    @Mapping(target = "parent", source = "parent.id")
    public abstract CategoryResponse toCategoryResponse(Category request);

    @Mapping(target = "parent", ignore = true)
    public abstract void updateCategory(@MappingTarget Category category, CategoryRequest request);

    @Mapping(target = "children", ignore = true)
    public abstract CategoryTreeResponse toTree(Category category);
}
