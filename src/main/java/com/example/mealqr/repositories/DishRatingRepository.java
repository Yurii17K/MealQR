package com.example.mealqr.repositories;

import com.example.mealqr.pojos.DishRating;
import io.vavr.Tuple2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DishRatingRepository extends JpaRepository<DishRating, Integer> {

    List<DishRating> findAllByDishId(Integer dishId);

    Optional<DishRating> findByDishIdAndUserEmail(Integer dishID, String userEmail);

    @Query(value = "select distinct dr.id, dr.dish_id, dr.user_email, dr.rating from dish_ratings as dr " +
            "join dishes as d on d.id = dr.dish_id " +
            "where dr.user_email = :user_email and d.restaurant_name = :restaurant_name " +
            "order by dr.dish_id", nativeQuery = true)
    List<DishRating> findAllByRestaurantNameAndUserEmail(
            @Param("user_email") String userEmail,
            @Param("restaurant_name") String restaurantName);
}
