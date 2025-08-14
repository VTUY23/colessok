package com.colessok.api.category.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Page<Category> findByParent(Category parentId, Pageable pageable);

    List<Category> findByParent(Category parentId);

    List<Category> findByIsDeletedFalse();
}
