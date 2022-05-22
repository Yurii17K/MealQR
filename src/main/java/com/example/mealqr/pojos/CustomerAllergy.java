package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@Entity(name = "CustomerAllergies")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAllergy {

    @Id
    @Email(message = "Email format is wrong")
    @Column(nullable = false)
    private String userEmail;

    @NotBlank
    @Column(nullable = false)
    private String allergies;
}
