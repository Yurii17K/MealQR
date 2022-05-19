package com.example.mealqr.repositories;

import com.example.mealqr.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
