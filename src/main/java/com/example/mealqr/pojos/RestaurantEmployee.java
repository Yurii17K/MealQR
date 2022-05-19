package com.example.mealqr.pojos;

import com.example.mealqr.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "RestaurantEmployees")
public class RestaurantEmployee extends User{

    @NotBlank
    private String restaurantName;

    public RestaurantEmployee(Integer ID, String name, String lastName, String city, String email, String pass, String restaurantName) {
        super(ID, name, lastName, city, email, pass, Role.RESTAURANT_EMPLOYEE);
        this.restaurantName = restaurantName;
    }
}

