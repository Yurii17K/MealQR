package com.example.mealqr.pojos;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RestaurantEmployee")
public class RestaurantEmployee {

    @Id
    @Column(nullable = false)
    private String userEmail;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String restaurantName;

}

