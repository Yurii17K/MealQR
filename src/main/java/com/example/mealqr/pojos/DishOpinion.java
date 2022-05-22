package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DishOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Column(nullable = false)
    private Integer ID;

    @NotNull
    @Column(nullable = false)
    private Integer dishId;

    @NotBlank
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String userEmail;
}
