package com.example.mealqr;
import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.pojos.QRData;
import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.*;
import com.example.mealqr.security.Roles;
import com.example.mealqr.services.CartItemService;
import com.example.mealqr.services.DishService;
import com.example.mealqr.services.QRDataService;
import com.example.mealqr.services.UserService;
import io.vavr.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        MockitoAnnotations.openMocks(this); //without this you will get NPE
    }

    @Test
    public void getCustomerCartCost(){
        doReturn(List.of(CartItem.builder().cartItemCost(BigDecimal.TEN).build(), CartItem.builder().cartItemCost(BigDecimal.ONE).build()))
                .when(cartItemRepository).getCustomerCart(anyString());

        Assertions.assertEquals(11.d, cartItemService.getCustomerCartCost("test@email.com"));
    }
}
