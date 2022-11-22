package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.web.rest.reponse.DishRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DishResMapper {

    public static DishRes mapToDishRes(Dish dish) {
        return DishRes.of(
                dish.getDishId(),
                dish.getDishName(),
                RestaurantResMapper.mapToRestaurantRes(dish.getRestaurant()),
                ImageDtoMapper.mapToImageDto(dish.getDishImage()),
                dish.getDishPrice(),
                dish.getDishDescription()
        );
    }
}
