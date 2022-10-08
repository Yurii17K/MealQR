package com.example.mealqr;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.services.CartItemService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.mealqr.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTests {

    @Mock
    private DishRepository dishRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void addDishToCustomerCartWhenDishIsNotPresentCaseInvalid() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(anyString(), anyString())).thenReturn(Option.none());
        //when
        Either<String, Dish> result = cartItemService.addDishToCustomerCart(USER_EMAIL, DISH_NAME, RESTAURANT_NAME);
        //then
        assertTrue(result.isLeft());
        assertThat(result.getLeft()).isEqualTo(SUCH_DISH_DOES_NOT_EXIST);
    }

    @Test
    void addDishToCustomerCartWhenDishIsNotInCustomerCartCaseValid() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.of(DISH));
        when(cartItemRepository.findByUserEmailAndDishDishId(USER_EMAIL, GLOBAL_ID_Integer)).thenReturn(Option.none());
        //when
        Either<String, Dish> result = cartItemService.addDishToCustomerCart(USER_EMAIL, DISH_NAME, RESTAURANT_NAME);
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).isEqualTo(DISH);
    }

    @Test
    void addDishToCustomerCartWhenDishIsInCustomerCartCaseValid() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.of(DISH));
        when(cartItemRepository.findByUserEmailAndDishDishId(USER_EMAIL, GLOBAL_ID_Integer)).thenReturn(Option.of(CART_ITEM));
        //when
        Either<String, Dish> result = cartItemService.addDishToCustomerCart(USER_EMAIL, DISH_NAME, RESTAURANT_NAME);
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).isEqualTo(DISH);
    }

    @Test
    void changeDishQuantityInCustomerCartWhenDishDoesNotExistCaseInvalid() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.none());
        //when
        Either<String, Boolean> result = cartItemService.changeDishQuantityInCustomerCart(USER_EMAIL, DISH_NAME,
                RESTAURANT_NAME, DISH_QUANTITY);
        //then
        assertTrue(result.isLeft());
        assertThat(result.getLeft()).isEqualTo(SUCH_DISH_DOES_NOT_EXIST);
    }

    @Test
    void changeDishQuantityInCustomerCartCaseValid() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.of(DISH));
        //when
        Either<String, Boolean> result = cartItemService.changeDishQuantityInCustomerCart(USER_EMAIL, DISH_NAME,
                RESTAURANT_NAME, DISH_QUANTITY);
        //then
        assertTrue(result.isRight());
        assertTrue(result.get());
    }

    @Test
    void deleteDishFromCustomerCartWhenDishDoesNotExistCaseInvalid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.none());
        //when
        Either<String, Boolean> result = cartItemService.deleteDishFromCustomerCart(USER_EMAIL, DISH_NAME, RESTAURANT_NAME);
        //then
        assertTrue(result.isLeft());
        assertThat(result.getLeft()).isEqualTo(SUCH_DISH_DOES_NOT_EXIST);
    }

    @Test
    void deleteDish(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_NAME, RESTAURANT_NAME)).thenReturn(Option.of(DISH));
        //when
        Either<String, Boolean> result = cartItemService.deleteDishFromCustomerCart(USER_EMAIL, DISH_NAME, RESTAURANT_NAME);
        //then
        assertTrue(result.isRight());
        assertTrue(result.get());
    }
}
