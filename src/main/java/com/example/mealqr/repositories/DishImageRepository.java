package com.example.mealqr.repositories;

import com.example.mealqr.domain.DishImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishImageRepository extends JpaRepository<DishImage, String> {
}
