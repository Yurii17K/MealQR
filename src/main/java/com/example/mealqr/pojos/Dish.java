package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Getter
@Entity(name = "Dishes")
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @NotBlank
    private String dishName;

    @NotBlank
    private byte[] dishImg;

    @NotBlank
    private double dishPrice;

    @NotBlank
    private String dishDescription;
}
