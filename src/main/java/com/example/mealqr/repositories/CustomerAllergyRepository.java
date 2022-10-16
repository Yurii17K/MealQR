package com.example.mealqr.repositories;

import com.example.mealqr.domain.CustomerAllergy;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAllergyRepository extends JpaRepository<CustomerAllergy, String> {
    Option<CustomerAllergy> findByUserEmail(String userEmail);
}
