package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    public Seq<CartItem> getCustomerCart(@NotBlank String userEmail) {
        return cartItemRepository.getCustomerCart(userEmail);
    }

    public double getCustomerCartCost(@NotBlank String userEmail) {
        return cartItemRepository.getCustomerCart(userEmail)//
                .map(cartItem -> cartItem.getCartItemCost().doubleValue())//
                .sum().doubleValue();
    }

    public boolean clearCustomerCart(@NotBlank String userEmail) {
        cartItemRepository.deleteAllByUserEmail(userEmail);
        return true;
    }

    public Either<String, Dish> addDishToCustomerCart(@NotBlank String userEmail, @NotBlank String dishName,
            @NotBlank String restaurantName) {
        return dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName)//
                .peek(dish -> cartItemRepository.findByUserEmailAndDishDishId(userEmail, dish.getDishId())//
                        .map(cartItem -> {
                            cartItemRepository.changeDishQuantityInCustomerCart(userEmail, dish.getDishId(), 1);
                            return dish;
                        })
                        .getOrElse(() -> {
                            cartItemRepository.addDishToCustomerCart(userEmail, dish.getDishId(), 1);
                            return dish;
                        }))
                .toEither(SUCH_DISH_DOES_NOT_EXIST);
    }

    public Either<String, Boolean> changeDishQuantityInCustomerCart(@NotBlank String userEmail, @NotBlank String dishName,
            @NotBlank String restaurantName, @NotNull int quantity) {
        return dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName)//
                .peek(dish -> cartItemRepository.changeDishQuantityInCustomerCart(userEmail, dish.getDishId(), quantity))//
                .map(dish -> true)//
                .toEither(SUCH_DISH_DOES_NOT_EXIST);
    }

    public Either<String, Boolean> deleteDishFromCustomerCart(@NotBlank String userEmail, @NotBlank String dishName,
            @NotBlank String restaurantName) {
        return dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName)//
                .peek(dish -> cartItemRepository.deleteByUserEmailAndDishDishId(userEmail, dish.getDishId()))//
                .map(dish -> true)//
                .toEither(SUCH_DISH_DOES_NOT_EXIST);
    }
}
