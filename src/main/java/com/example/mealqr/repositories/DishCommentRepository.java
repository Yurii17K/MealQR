package com.example.mealqr.repositories;

import com.example.mealqr.domain.DishComment;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishCommentRepository extends JpaRepository<DishComment, Integer> {

    Seq<DishComment> findAllByDishDishId(String dishID);

    Option<DishComment> findByDishDishIdAndUserEmail(String dishID, String userEmail);}
