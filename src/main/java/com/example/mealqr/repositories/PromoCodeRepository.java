package com.example.mealqr.repositories;

import com.example.mealqr.domain.PromoCode;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    Option<PromoCode> findByPromoCodeStringAndRestaurantRestaurantId(String promoCodeString, String restaurantId);
}
