package com.colessok.api.file.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.colessok.api.identity.entity.UserProfile;
import com.colessok.api.recipe.entity.Recipe;
import com.colessok.api.recipe.entity.RecipeStep;
import com.colessok.api.review.entity.ReviewFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false)
    String id;

    @Column(nullable = false)
    String contentType;

    @Column(nullable = false)
    long size;

    @Column(nullable = false)
    String md5Checksum;

    @Column(nullable = false, unique = true)
    String path;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    boolean isDeleted = false;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    UserProfile profile;

    @OneToOne(mappedBy = "media", fetch = FetchType.LAZY)
    RecipeStep recipeStep;

    @OneToOne(mappedBy = "banner", fetch = FetchType.LAZY)
    Recipe recipe;

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    ReviewFile media;
}
