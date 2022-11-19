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

    @OneToOne(targetEntity = DishImage.class)
    @JoinColumn(name = "dish_image_id", referencedColumnName = "dish_image_id")
    DishImage dishImage;

    BigDecimal dishPrice;
    String dishDescription;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static Dish of(DishSaveReq dishSaveReq, DishImage dishImage) {
        return Dish.builder()//
                .dishId(UUID.randomUUID().toString())//
                .dishName(dishSaveReq.getDishName())//
                .restaurant(Restaurant.builder().restaurantId(dishSaveReq.getRestaurantId()).build())//
                .dishDescription(dishSaveReq.getDishDescription())//
                .dishPrice(dishSaveReq.getDishPrice())//
                .dishImage(dishImage)
                .build();
    }

    public static Dish of(DishUpdateReq dishUpdateReq, Dish originalDish) {
        return Dish.builder()//
                .dishId(originalDish.getDishId())//
                .dishPrice(dishUpdateReq.getDishPrice().getOrElse(originalDish.getDishPrice()))//
                .dishDescription(dishUpdateReq.getDishDescription().getOrElse(originalDish.getDishDescription()))//
                .dishImage(dishUpdateReq.getDishImage()//
                        .map(d -> DishImage.of(d, originalDish.getDishImage()))//
                        .getOrElse(originalDish.getDishImage()))//
                .build();
    }
}
