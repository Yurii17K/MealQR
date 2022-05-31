package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CartItems")
@Builder
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
    private BigDecimal cartItemCost;

}
