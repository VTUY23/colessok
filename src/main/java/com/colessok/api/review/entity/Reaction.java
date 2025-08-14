package com.colessok.api.review.entity;

import jakarta.persistence.*;

import com.colessok.api.identity.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "reactions")
public class Reaction {
    @EmbeddedId
    ReactionId id;

    boolean description;

    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", columnDefinition = "CHAR(36)")
    Review review;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    User user;
}
