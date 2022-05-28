package com.example.mealqr.repositories;

import com.example.mealqr.pojos.RestaurantEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantEmployeeRepository extends JpaRepository<RestaurantEmployee, String> {

    Optional<RestaurantEmployee> findByUserEmail(String userEmail);

    boolean existsByRestaurantName(String restaurantName);
}
