package com.example.mealqr.repositories;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.PromoCode;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {
    Seq<PromoCode> findByDish(Dish dish);

    Option<PromoCode> findByPromoCodeStringAndRestaurantRestaurantId(String promoCodeString, String restaurantId);
}
