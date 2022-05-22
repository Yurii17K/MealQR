package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DishRatings")
public class DishRating extends DishOpinion{

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;
}
