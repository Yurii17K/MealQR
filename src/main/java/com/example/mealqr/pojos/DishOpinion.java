package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DishOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer ID;

    @NotNull
    @Column(nullable = false)
    private Integer dishID;

    @NotBlank
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String userEmail;
}
