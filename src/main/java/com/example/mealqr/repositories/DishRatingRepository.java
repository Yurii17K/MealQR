package com.example.mealqr.repositories;

import com.example.mealqr.pojos.DishComment;
import com.example.mealqr.pojos.DishRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
