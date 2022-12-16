package com.example.mealqr.repositories;

import com.example.mealqr.domain.Restaurant;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    Option<Restaurant> findByRestaurantId(String restaurantId);

    Seq<Restaurant> findAllByRestaurantIdIn(Set<String> restaurantIds);

    Seq<Restaurant> findAllByRestaurantManagerEmail(String email);

    Seq<Restaurant> findAllByRestaurantCity(String restaurantCity);
}
