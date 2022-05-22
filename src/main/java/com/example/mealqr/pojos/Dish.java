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
    @Column(unique = true, nullable = false)
    private String dishName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String restaurantName;

    @NotNull
    @Column(nullable = false)
    private byte[] dishImg;

    @NotNull
    @Column(nullable = false)
    private double dishPrice;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String dishDescription;
}
