package com.example.mealqr.repositories;

import com.example.mealqr.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String userEmail);

    List<User> findAllByRole(String role);
}
