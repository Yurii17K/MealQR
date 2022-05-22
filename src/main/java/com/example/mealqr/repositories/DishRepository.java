package com.example.mealqr.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mealqr.pojos.Dish;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> findAllByRestaurantName(String restaurantName);

    Optional<Dish> findByDishNameAndRestaurantName(String dishName, String restaurantName);

    Optional<Dish> findByID(Integer dishID);

    @Transactional
    void deleteByDishNameAndRestaurantName(String dishName, String restaurantName);
}
