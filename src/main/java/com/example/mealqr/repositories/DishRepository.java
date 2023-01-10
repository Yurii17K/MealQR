package com.example.mealqr.repositories;


import com.example.mealqr.domain.Dish;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, String> {

    List<Dish> findAllByRestaurantRestaurantId(String restaurantId);

    Option<Dish> findByDishNameAndRestaurantRestaurantId(String dishName, String restaurantId);

    Option<Dish> findByDishId(String dishID);

    void deleteByDishNameAndRestaurantRestaurantId(String dishName, String restaurantId);
}
