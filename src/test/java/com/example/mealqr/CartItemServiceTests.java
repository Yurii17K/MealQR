package com.example.mealqr;
import com.example.mealqr.pojos.CartItem;
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
import java.util.Collections;
import java.util.List;

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

    //TODO: 3 MORE TESTS
}
