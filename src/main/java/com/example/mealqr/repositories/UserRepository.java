package com.example.mealqr.repositories;

import com.example.mealqr.pojos.User;
import com.example.mealqr.security.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String userEmail);

    List<User> findAllByRole(Roles role);

    @Query(value = "select u.email from users as u " +
            "join dish_ratings as dr on u.email = dr.user_email " +
            "join dishes as d on dr.dish_id = d.id " +
            "where d.restaurant_name = :restaurant_name and u.role = 'CUSTOMER'",
            nativeQuery = true)
    List<String> findAllCustomersWhoRatedAnythingInRestaurant(@Param("restaurant_name") String restaurantName);
}
