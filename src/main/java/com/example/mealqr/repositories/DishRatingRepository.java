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

    
}
