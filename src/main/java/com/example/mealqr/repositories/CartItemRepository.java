package com.example.mealqr.repositories;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query(value = "select * from cart_items as ci " +
            "join dishes as d on d.dish_id = ci.dish_id " +
            "where ci.user_email = :user_email",
    nativeQuery = true)
    Seq<CartItem> getCustomerCart(@Param("user_email") String userEmail);

    Option<CartItem> findByUserEmailAndDishDishId(String userEmail, String dishID);

    Seq<CartItem> findAllByDish(Dish dish);

    void deleteAllByUserEmail(String userEmail);

    void deleteByUserEmailAndDishDishId(String userEmail, String dishID);

    void deleteByUserEmailAndDishRestaurantRestaurantId(String userEmail, String restaurantId);

    @Transactional
    @Query(value = "SELECT count(*) FROM change_dish_quantity_in_customer_cart(:user_email_param, :dish_id_param, :quantity_param)",
            nativeQuery = true)
    void increaseDishQuantityInCustomerCart(@Param("user_email_param") String userEmail,
                                          @Param("dish_id_param") String dishID,
                                          @Param("quantity_param") int quantity);

    @Transactional
    @Query(value = "SELECT count(*) FROM add_dish_to_customer_cart(:new_cart_item_id, :user_email_param, :dish_id_param, :quantity_param)",
            nativeQuery = true)
    void addDishToCustomerCart(@Param("new_cart_item_id") String cartItemId,
                                @Param("user_email_param") String userEmail,
                               @Param("dish_id_param") String dishID,
                               @Param("quantity_param") int quantity);
}
