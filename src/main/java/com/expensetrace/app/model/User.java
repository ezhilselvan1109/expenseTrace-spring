package com.expensetrace.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;

    @Column(nullable = false)
    private boolean enabled = false;

    private String verificationToken;

    private String otpCode;

    private LocalDateTime otpExpiry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Category> userCategories = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Settings settings;
}