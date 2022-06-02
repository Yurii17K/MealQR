package com.example.mealqr.pojos;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@With
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
