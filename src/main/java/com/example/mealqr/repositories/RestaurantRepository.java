package com.example.mealqr.repositories;

import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.domain.User;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    Option<Restaurant> findByRestaurantId(String restaurantId);

    Seq<Restaurant> findAllByRestaurantManager(User restaurantManager);
}
