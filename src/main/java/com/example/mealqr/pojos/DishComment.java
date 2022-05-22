package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DishComments")
public class DishComment extends DishOpinion{

    @NotNull
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String comment;
}
