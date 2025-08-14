package com.colessok.api.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.colessok.api.category.dto.request.CategoryRequest;
import com.colessok.api.category.dto.response.CategoryResponse;
import com.colessok.api.category.dto.response.CategoryTreeResponse;
import com.colessok.api.category.service.CategoryService;
import com.colessok.api.common.dto.ApiResponse;
import com.colessok.api.common.dto.PageResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(null, request))
                .build();
    }

    @PostMapping("/{id}")
    ApiResponse<CategoryResponse> createCategoryByParent(
            @PathVariable("id") String id, @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(id, request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable("id") String id, @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @GetMapping("/admin")
    ApiResponse<PageResponse<CategoryResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .result(categoryService.getAll(null, page, size))
                .build();
    }

    @GetMapping("/admin/{id}")
    ApiResponse<PageResponse<CategoryResponse>> getAllParent(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .result(categoryService.getAll(id, page, size))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryTreeResponse>> getAllForUser() {
        return ApiResponse.<List<CategoryTreeResponse>>builder()
                .result(categoryService.getCategoryTree(null))
                .build();
    }

    @GetMapping("/{parentId}")
    ApiResponse<List<CategoryTreeResponse>> getCategoriesForUser(@PathVariable String parentId) {
        return ApiResponse.<List<CategoryTreeResponse>>builder()
                .result(categoryService.getCategoryTree(parentId))
                .build();
    }

    @DeleteMapping("{id}")
    ApiResponse<String> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder().result("Category has been deleted").build();
    }
}
