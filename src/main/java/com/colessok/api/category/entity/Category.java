package com.colessok.api.category.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.colessok.api.recipe.entity.Ingredient;
import com.colessok.api.recipe.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false)
    String id;

    @Column(nullable = false, unique = true)
    String name;

    Boolean type;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<Category> children = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    Set<Recipe> recipes = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<Ingredient> ingredients = new HashSet<>();
}
