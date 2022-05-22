package com.example.mealqr.repositories;

import com.example.mealqr.pojos.CustomerAllergy;
import com.example.mealqr.pojos.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAllergyRepository extends JpaRepository<CustomerAllergy, String> {
    Optional<CustomerAllergy> findByUserEmail(String userEmail);
}
