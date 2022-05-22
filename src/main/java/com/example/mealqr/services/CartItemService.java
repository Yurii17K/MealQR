package com.example.mealqr.services;

import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;

    public List<CartItem> getCustomerCart(@NotBlank String userEmail) {
        return cartItemRepository.getCustomerCart(userEmail);
    }

    public double getCustomerCartCost(@NotBlank String userEmail) {
        return cartItemRepository.getCustomerCart(userEmail).stream().mapToDouble(CartItem::getCartItemCost).sum();
    }

    public Tuple2<Boolean, String> clearCustomerCart(@NotBlank String userEmail) {
        cartItemRepository.deleteAllByUserEmail(userEmail);

        if(!cartItemRepository.getCustomerCart(userEmail).isEmpty()) {
            return Tuple.of(false, "Customer cart not cleared");
        } else {
            return Tuple.of(true, "Customer cart cleared");
        }
    }

    public Tuple2<Boolean, String> addDishToCustomerCart(@NotBlank String userEmail, @NotBlank String dishName,
                                                         @NotBlank String restaurantName) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        // if for some reason the dish already exists in customer cart -> modify it
        if (cartItemRepository
                .findByUserEmailAndDishId(userEmail, optionalDish.get().getID())
                .isPresent()) {
            cartItemRepository
                    .changeDishQuantityInCustomerCart(userEmail, optionalDish.get().getID(), 1);
        } else { // otherwise, just add it
            cartItemRepository
                    .addDishToCustomerCart(userEmail, optionalDish.get().getID(), 1);
        }

        return Tuple.of(true, "Dish added to customer cart");
    }

    public Tuple2<Boolean, String> changeDishQuantityInCustomerCart(
            @NotBlank String userEmail, @NotBlank String dishName, @NotBlank String restaurantName, @NotNull int quantity
    ) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        cartItemRepository.changeDishQuantityInCustomerCart(userEmail, optionalDish.get().getID(), quantity);
        return Tuple.of(true, "Added " + quantity + " of " + dishName + " from " + restaurantName);
    }

    public Tuple2<Boolean, String> deleteDishFromCustomerCart(
            @NotBlank String userEmail, @NotBlank String dishName, @NotBlank String restaurantName
    ) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        cartItemRepository.deleteByUserEmailAndAndDishId(userEmail, optionalDish.get().getID());
        return Tuple.of(true, "Removed " + dishName + " of " + restaurantName + " from customer cart");
    }
}
