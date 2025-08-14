package com.colessok.api.recipe.entity;

import jakarta.persistence.*;

import com.colessok.api.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "name"}), name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false)
    String id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, columnDefinition = "float default 0.01")
    float quantity;

    @Column(length = 20, nullable = false)
    String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    Recipe recipe;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "categoryId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    Category category;
}
