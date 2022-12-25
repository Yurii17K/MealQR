package com.example.mealqr.repositories;

import com.example.mealqr.domain.DishRating;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRatingRepository extends JpaRepository<DishRating, String> {

    Seq<DishRating> findAllByDishDishId(String dishId);

    Option<DishRating> findByDishDishIdAndUserEmail(String dishID, String userEmail);
    Seq<DishRating> findAllByUserEmail(String userEmail);

    @Query(value = "select distinct dr.dish_opinion_id, dr.dish_id, dr.user_email, dr.rating from dish_ratings as dr " +
            "join dishes as d on d.dish_id = dr.dish_id " +
            "where dr.user_email = :user_email and d.restaurant_id = :restaurant_id " +
            "order by dr.dish_id", nativeQuery = true)
    Seq<DishRating> findAllByUserEmailAndRestaurant(
            @Param("user_email") String userEmail,
            @Param("restaurant_id") String restaurantId);


    @Modifying
    @Query("delete from DishRatings d where d.dish.dishId=:dishId")
    Integer deleteByDishId(@Param("dishId") String dishId);
}
