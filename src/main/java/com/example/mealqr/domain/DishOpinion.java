package com.example.mealqr.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class DishOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dish_opinion_id")
    Integer dishOpinionId;

    @ManyToOne(targetEntity = Dish.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    String userEmail;
}
