package com.example.mealqr.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DishRatings")
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRating extends DishOpinion{
    int rating;
}
