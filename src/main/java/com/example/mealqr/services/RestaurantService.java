package com.example.mealqr.services;

import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.repositories.RestaurantRepository;
import com.example.mealqr.services.mappers.RestaurantResMapper;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantRes createRestaurant(String userEmail, RestaurantSaveReq restaurantSaveReq) {
        return RestaurantResMapper.mapToRestaurantRes(restaurantRepository.save(
                Restaurant.of(restaurantSaveReq, userEmail)));
    }

    public List<RestaurantRes> getRestaurants(Set<String> restaurantIds) {
        return restaurantRepository.findAllByRestaurantIdIn(restaurantIds)//
                .map(RestaurantResMapper::mapToRestaurantRes)//
                .asJava();
    }

    public List<RestaurantRes> getRestaurantsByCity(String restaurantCity) {
        return restaurantRepository.findAllByRestaurantCity(restaurantCity)//
                .map(RestaurantResMapper::mapToRestaurantRes)//
                .asJava();
    }

    public Set<String> getRestaurantCities() {
        return restaurantRepository.findAll()//
                .stream()//
                .map(Restaurant::getRestaurantCity)//
                .collect(Collectors.toSet());
    }
}
