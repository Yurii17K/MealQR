package com.example.mealqr.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "dish_image")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishImage {

    @Id
    @Column(name = "dish_image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer dishImageId;

    @Transient
    @OneToOne(targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    byte[] data;
    String contentType;
}
