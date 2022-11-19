package com.example.mealqr.services;

import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.repositories.RestaurantRepository;
import com.example.mealqr.services.mappers.RestaurantResMapper;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantRes createRestaurant(String userEmail, RestaurantSaveReq restaurantSaveReq) {
        return RestaurantResMapper.mapToRestaurantRes(restaurantRepository.save(
                Restaurant.of(restaurantSaveReq, userEmail)));
    }
}
