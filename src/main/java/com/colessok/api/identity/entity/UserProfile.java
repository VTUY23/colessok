package com.colessok.api.identity.entity;

import java.time.LocalDate;

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
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)", updatable = false)
    String id;

    String firstName;

    String lastName;

    Boolean gender;

    @Column(length = 20)
    String phone;

    @Column(columnDefinition = "TEXT")
    String address;

    @Column(nullable = false)
    LocalDate dob;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avatar")
    File avatar;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    User user;
}
