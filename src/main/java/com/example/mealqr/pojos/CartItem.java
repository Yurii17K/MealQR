package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @Email(message = "Email should be valid")
    private String customerEmail;

    @NotBlank
    private String dishName;

    @Min(1)
    private int dishQuantity;

    @NotBlank
    private double cartItemCost;

}
