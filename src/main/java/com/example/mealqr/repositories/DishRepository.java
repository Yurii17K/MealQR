package com.example.mealqr.repositories;


import com.example.mealqr.domain.Dish;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    Seq<Dish> findAllByRestaurantRestaurantId(String restaurantId);

    Option<Dish> findByDishNameAndRestaurantRestaurantId(String dishName, String restaurantId);

    Option<Dish> findByDishId(Integer dishID);

    @Transactional
    void deleteByDishNameAndRestaurantRestaurantId(String dishName, String restaurantId);
}
