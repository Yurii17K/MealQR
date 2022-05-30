package com.example.mealqr;

import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.services.CartItemService;
import com.example.mealqr.services.UserService;
import io.vavr.Tuple;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class MealQrApplicationTests {

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //without this you will get NPE
    }


    @Test
    void contextLoads() {
    }

    @Test
    void properNameHere() {
        // this will return a dummy list when getCustomerCart is called to imitate repository behaviour
        when(dishRepository.findByDishNameAndRestaurantName(anyString(), anyString())).thenReturn(Optional.empty());

        // rather useless example test
        Assertions.assertEquals(Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST), cartItemService.addDishToCustomerCart("as", "asd", "asd"));
    }

}
