package com.example.mealqr;
import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.repositories.*;
import com.example.mealqr.services.CartItemService;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartItemServiceTests {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    @Test
    public void getCustomerCartCost(){
        doReturn(List.of(CartItem.builder().cartItemCost(BigDecimal.TEN).build(), CartItem.builder().cartItemCost(BigDecimal.ONE).build()))
                .when(cartItemRepository).getCustomerCart(anyString());

        Assertions.assertEquals(11.d, cartItemService.getCustomerCartCost("test@email.com"));
    }

    @Test
    public void getClearCustomerCartWhenCartNotClearedInvalid() {
        //given
        when(cartItemRepository.getCustomerCart(anyString())).thenReturn(Collections.singletonList(new CartItem()));

        //when
        Tuple2<Boolean, String> result = cartItemService.clearCustomerCart("user email");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, "Customer cart not cleared");
        assertEquals(expected, result);
    }

    @Test
    public void getClearCustomerCartValid() {
        //given
        when(cartItemRepository.getCustomerCart(anyString())).thenReturn(Collections.emptyList());

        //when
        Tuple2<Boolean, String> result = cartItemService.clearCustomerCart("user email");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Customer cart cleared");
        assertEquals(expected, result);
    }

    @Test
    public void addDishToCustomerCartButDishIsEmpty() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.empty());

        //when
        Tuple2<Boolean, String> result = cartItemService.addDishToCustomerCart("test mail", "test dish", "test restaurant");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST);
        assertEquals(expected, result);
    }


    @Test
    public void modifyDishInCart() {
        //given
        Dish testDish = new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test");
        CartItem testCartItem = new CartItem(1, "testUserEmail", 1, 5, BigDecimal.valueOf(6.00));
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));
        when(cartItemRepository.findByUserEmailAndDishId(anyString(), anyInt())).thenReturn(Optional.of(testCartItem));

        //when
        Tuple2<Boolean, String> result = cartItemService.addDishToCustomerCart("test mail", "test dish", "test restaurant");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Dish added to customer cart");
        assertEquals(expected, result);
    }
    @Test
    public void addDishToCart() {
        //given
        Dish testDish = new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test");
        CartItem testCartItem = new CartItem(1, "testUserEmail", 1, 5, BigDecimal.valueOf(6.00));
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));
        when(cartItemRepository.findByUserEmailAndDishId(anyString(), anyInt())).thenReturn(Optional.empty());

        //when
        Tuple2<Boolean, String> result = cartItemService.addDishToCustomerCart("test mail", "test dish", "test restaurant");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Dish added to customer cart");
        assertEquals(expected, result);
    }

    @Test
    public void changeDishQuantityButDishDoesntExist() {
        //given
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.empty());


        //when
        Tuple2<Boolean, String> result = cartItemService.changeDishQuantityInCustomerCart("test mail", "test dish", "test restaurant", 5);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST);
        assertEquals(expected, result);
    }

    @Test
    public void changeDishQuantity() {
        //given
        Dish testDish = Dish.builder().ID(5).dishName("Test dish").build();
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));

        //when
        Tuple2<Boolean, String> result = cartItemService.changeDishQuantityInCustomerCart("test mail", "test dish", "test restaurant", 5);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Added " + 5 + " of " + "test dish" + " from " + "test restaurant");
        assertEquals(expected, result);
    }

    @Test
    public void deleteDishButItDoesNotExist(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.empty());

        //when
        Tuple2<Boolean, String> result = cartItemService.deleteDishFromCustomerCart("test mail", "test dish", "test restaurant");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST);
        assertEquals(expected, result);

    }

    @Test
    public void deleteDish(){
        //given
        Dish testDish = Dish.builder().ID(5).dishName("Test dish").build();
        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));

        //when
        Tuple2<Boolean, String> result = cartItemService.deleteDishFromCustomerCart("test mail", "test dish", "test restaurant");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Removed " + "test dish" + " of " + "test restaurant" + " from customer cart");


        assertEquals(expected, result);

    }

}
