package com.example.mealqr.repositories;

import com.example.mealqr.pojos.DishRating;
import io.vavr.Tuple2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishRatingRepository extends JpaRepository<DishRating, Integer> {

    @Query(value = "select * from dish_ratings " +
            "where dish_id = :dish_id", nativeQuery = true)
    List<DishRating> findAllByDishID(@Param("dish_id") Integer dishID);

    @Query(value = "select * from dish_ratings " +
            "where dish_id = :dish_id AND user_email = :user_email", nativeQuery = true)
    Optional<DishRating> findByDishIDAndUserEmail(
            @Param("dish_id") Integer dishID,
            @Param("user_email") String userEmail);

    @Query(value = "select dr.user_email, dr.dish_id, dr.rating from dish_ratings as dr " +
            "right join dishes as d on dr.dish_id = d.id " +
            "where d.restaurant_name = :restaurant_name", nativeQuery = true)
    List<Tuple2<Integer, Integer>> findAllByRestaurant(
            @Param("restaurant_name") String restaurantName);
}
