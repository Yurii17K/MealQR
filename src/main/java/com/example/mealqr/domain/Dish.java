package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.DishSaveReq;
import com.example.mealqr.web.rest.request.DishUpdateReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@Entity(name = "Dishes")
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dish {

    @Id
    @Column(name = "dish_id")
    String dishId;

    String dishName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurant_id")
    @ToString.Exclude
    Restaurant restaurant;

    @OneToOne(targetEntity = DishImage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_image_id", referencedColumnName = "dish_image_id")
    DishImage dishImage;

    BigDecimal dishPrice;
    String dishDescription;

    public static Dish create(DishSaveReq dishSaveReq) {
        return Dish.builder()//
                .dishId(UUID.randomUUID().toString())//
                .dishName(dishSaveReq.getDishName())//
                .restaurant(Restaurant.builder().restaurantId(dishSaveReq.getRestaurantId()).build())//
                .dishDescription(dishSaveReq.getDishDescription())//
                .dishPrice(dishSaveReq.getDishPrice())//
                .dishImage(DishImage.of(dishSaveReq))//
                .build();
    }

    public static Dish update(DishUpdateReq dishUpdateReq, Dish originalDish) {
        return Dish.builder()//
                .dishId(originalDish.getDishId())//
                .dishName(dishUpdateReq.getDishName().getOrElse(originalDish.getDishName()))//
                .dishPrice(dishUpdateReq.getDishPrice().getOrElse(originalDish.getDishPrice()))//
                .dishDescription(dishUpdateReq.getDishDescription().getOrElse(originalDish.getDishDescription()))//
                .dishImage(dishUpdateReq.getDishImage()//
                        .map(newDishImage -> DishImage.update(newDishImage, originalDish.getDishImage()))//
                        .getOrElse(originalDish.getDishImage()))//
                .build();
    }
}
