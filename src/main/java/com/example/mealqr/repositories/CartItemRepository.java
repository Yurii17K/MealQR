package com.example.mealqr.repositories;

import com.example.mealqr.pojos.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findAllByCustomerMail(String customerEmail);

    @Transactional
    void deleteAllByCustomerMail(String customerEmail);
}
