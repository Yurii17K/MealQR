package com.example.mealqr.repositories;

import com.example.mealqr.pojos.DishComment;
import com.example.mealqr.pojos.DishRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishCommentRepository extends JpaRepository<DishComment, Integer> {

    @Query(value = "select * from dish_comments " +
            "where dish_id = :dish_id", nativeQuery = true)
    List<DishComment> findAllByDishID(@Param("dish_id") Integer dishID);

    @Query(value = "select * from dish_comments " +
            "where dish_id = :dish_id AND customer_email = :user_email", nativeQuery = true)
    Optional<DishComment> findByDishIDAndUserEmail(
            @Param("dish_id") Integer dishID,
            @Param("user_email") String userEmail);}
