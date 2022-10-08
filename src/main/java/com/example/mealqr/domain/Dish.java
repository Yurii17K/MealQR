package com.example.mealqr.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
@Entity(name = "Dishes")
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dish_id")
    Integer dishId;

    String dishName;
    String restaurantName;

    @OneToOne(targetEntity = DishImage.class)
    @JoinColumn(name = "dish_image_id", referencedColumnName = "dish_image_id")
    DishImage dishImage;

    BigDecimal dishPrice;
    String dishDescription;
}
