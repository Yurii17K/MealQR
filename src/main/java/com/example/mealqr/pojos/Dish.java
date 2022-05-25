package com.example.mealqr.pojos;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
@Entity(name = "Dishes")
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer ID;

    @NotBlank
    @Column(nullable = false)
    private String dishName;

    @NotBlank
    @Column(nullable = false)
    private String restaurantName;

    @NotNull
    @Column(nullable = false)
    private byte[] dishImg;

    @NotNull
    @Column(nullable = false)
    private double dishPrice;

    @NotBlank
    @Column(nullable = false)
    private String dishDescription;
}
