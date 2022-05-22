package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
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

