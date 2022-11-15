package com.example.mealqr.repositories;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.PromoCode;
import io.vavr.collection.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {
    List<PromoCode> findByDish(Dish dish);
}
