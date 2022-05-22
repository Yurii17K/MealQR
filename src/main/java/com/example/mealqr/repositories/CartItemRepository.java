package com.example.mealqr.repositories;

import com.example.mealqr.pojos.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query(value = "select * from cart_items as ci " +
            "join dishes as d on d.id = ci.dish_id " +
            "where ci.user_email = :user_email",
    nativeQuery = true)
    List<CartItem> getCustomerCart(@Param("user_email") String userEmail);

    Optional<CartItem> findByUserEmailAndDishId(String userEmail, Integer dishID);

    @Transactional
    void deleteAllByUserEmail(String userEmail);

    @Transactional
    void deleteByUserEmailAndAndDishId(String userEmail, Integer dishID);

    @Transactional
    @Query(value = "SELECT count(*) FROM change_dish_quantity_in_customer_cart(:user_email_param, :dish_id_param, :quantity_param)",
            nativeQuery = true)
    void changeDishQuantityInCustomerCart(@Param("user_email_param") String userEmail,
                                          @Param("dish_id_param") Integer dishID,
                                          @Param("quantity_param") int quantity);

    @Transactional
    @Query(value = "SELECT count(*) FROM add_dish_to_customer_cart(:user_email_param, :dish_id_param, :quantity_param)",
            nativeQuery = true)
    void addDishToCustomerCart(@Param("user_email_param") String userEmail,
                               @Param("dish_id_param") Integer dishID,
                               @Param("quantity_param") int quantity);
}
