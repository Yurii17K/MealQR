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

    List<DishComment> findAllByDishId(Integer dishID);

    Optional<DishComment> findByDishIdAndUserEmail(Integer dishID, String userEmail);}
