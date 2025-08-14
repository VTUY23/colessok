package com.colessok.api.review.entity;

import jakarta.persistence.*;

import com.colessok.api.file.entity.File;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "reviewFiles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReviewFile {
    @Id
    @Column(name = "file_id", columnDefinition = "CHAR(36)")
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Review review;
}
