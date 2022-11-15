package com.example.mealqr.repositories;

import com.example.mealqr.domain.User;
import com.example.mealqr.security.Roles;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Option<User> findUserByEmail(String userEmail);

    Seq<User> findAllByRole(Roles role);

    @Query(value = "select u.email from users as u " +
            "join dish_ratings as dr on u.email = dr.user_email " +
            "join dishes as d on dr.dish_id = d.dish_id " +
            "where d.restaurant_id = :restaurant_id and u.role = 'CUSTOMER'",
            nativeQuery = true)
    Seq<String> findAllCustomersWhoRatedSomethingInRestaurant(@Param("restaurant_id") String restaurantId);
}
