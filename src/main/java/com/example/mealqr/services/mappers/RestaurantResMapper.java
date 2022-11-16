package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantResMapper {

    public static RestaurantRes mapToRestaurantRes(Restaurant restaurant) {
        return RestaurantRes.of(
                restaurant.getRestaurantId(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantCity(),
                UserResMapper.mapToUserRes(restaurant.getRestaurantManager())
        );
    }
}
