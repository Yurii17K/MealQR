package com.example.mealqr.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dish_comments")
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishComment extends DishOpinion{
    private String comment;
}
