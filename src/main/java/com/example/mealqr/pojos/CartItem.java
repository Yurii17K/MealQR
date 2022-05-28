package com.example.mealqr.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@Entity(name = "CartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer ID;

    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String userEmail;

    @NotNull
    @Column(nullable = false)
    private Integer dishId;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int dishQuantity;

    @NotNull
    @Column(nullable = false)
    private double cartItemCost;

}
