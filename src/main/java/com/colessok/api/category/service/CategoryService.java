package com.colessok.api.category.service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.colessok.api.category.dto.request.CategoryRequest;
import com.colessok.api.category.dto.response.CategoryResponse;
import com.colessok.api.category.dto.response.CategoryTreeResponse;
import com.colessok.api.category.entity.Category;
import com.colessok.api.category.mapper.CategoryMapper;
import com.colessok.api.category.repository.CategoryRepository;
import com.colessok.api.common.dto.PageResponse;
import com.colessok.api.common.exception.AppException;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(String parentId, CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        if (parentId != null && !"null".equals(parentId)) {
            Category parent = categoryRepository.findById(parentId).orElseThrow(() -> new AppException(null));
            category.setType(parent.getType());
            category.setParent(parent);
        }
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(null));

        categoryMapper.updateCategory(category, request);
        for (Category child : category.getChildren()) child.setType(request.getType());

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public List<CategoryTreeResponse> getCategoryTree(String parentId) {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();

        // B1: Map Category -> Tree DTO (không gán children ở đây)
        Map<String, CategoryTreeResponse> nodeMap = new HashMap<>();
        for (Category category : categories) {
            nodeMap.put(category.getId(), categoryMapper.toTree(category));
        }

        // B2: Gắn con vào cha
        List<CategoryTreeResponse> roots = new ArrayList<>();
        for (Category category : categories) {
            CategoryTreeResponse currentNode = nodeMap.get(category.getId());

            if (category.getParent() != null) {
                CategoryTreeResponse parentNode =
                        nodeMap.get(category.getParent().getId());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) parentNode.setChildren(new ArrayList<>());
                    parentNode.getChildren().add(currentNode);
                }
            } else {
                roots.add(currentNode); // node gốc
            }
        }

        // B3: Trả về toàn bộ cây hoặc cây bắt đầu từ parentId
        if (parentId != null && nodeMap.containsKey(parentId)) {
            return List.of(nodeMap.get(parentId));
        }

        return roots;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<CategoryResponse> getAll(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "isDeleted"));
        Page<Category> pageData;
        if (id == null) pageData = categoryRepository.findAll(pageable);
        else {
            var parent = categoryRepository.findById(id).orElse(null);
            pageData = categoryRepository.findByParent(parent, pageable);
        }

        var recipeList =
                pageData.stream().map(categoryMapper::toCategoryResponse).toList();

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(recipeList)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
