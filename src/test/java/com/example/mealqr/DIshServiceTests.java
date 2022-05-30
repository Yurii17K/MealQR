package com.example.mealqr;
import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.pojos.QRData;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.services.CartItemService;
import com.example.mealqr.services.DishService;
import com.example.mealqr.services.QRDataService;
import io.vavr.Tuple;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DIshServiceTests {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //without this you will get NPE
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void getsAllDishesInRestaurant() {
        when(dishRepository.findAllByRestaurantName(anyString())).thenReturn(new ArrayList<>());
        //when(dishRepository.findByID(anyInt())).thenReturn(Optional.empty());


        Assertions.assertEquals(new ArrayList<Dish>(),dishService.getAllDishesInRestaurant("someRestaurant"));
    }

    @Test
    public void addDishButItExists(){

        doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
        //when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString()).isPresent()).thenReturn(Optional.empty());

        Assertions.assertEquals(Tuple.of(false,"Dish with this name already exists in this restaurant offer"),dishService.addDishToRestaurantOffer(new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
    }

    @Test
    public void addDish(){
        doReturn(Optional.empty()).doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
        String dishName = "Test name";
        String restaurantName = "Restaurant name";
        Assertions.assertEquals(Tuple.of(true,"Dish " + dishName + " added to " + restaurantName + " offer"),dishService.addDishToRestaurantOffer(new Dish(1,dishName,restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));

    }

    @Test
    public void removeDishButItDoesNotExist(){
        doReturn(Optional.empty()).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
        Assertions.assertEquals(Tuple.of(false, "Dish with this name does not exist in this restaurant offer"),dishService.removeDishFromRestaurantOffer("Test dish", "Test restaurant"));

    }

    @Test
    public void updateDishButItDoesNotExist(){
        doReturn(Optional.empty()).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
        String restaurantName = "Restaurant name";

        Assertions.assertEquals(Tuple.of(false, "No such dish in " + restaurantName + " offer"),dishService.updateDishInRestaurantOffer(new Dish(1,"Test",restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
    }
    @Test
    public void updateDish(){
        doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
        String dishName = "Test name";
        String restaurantName = "Restaurant name";
        Assertions.assertEquals(Tuple.of(true, "Dish " + dishName + " data updated in " + restaurantName + " offer"),dishService.updateDishInRestaurantOffer(new Dish(1,dishName,restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
    }

    @Test
    public void getRandomDish(){
        when(dishRepository.findAll()).thenReturn(new ArrayList<Dish>());

        Assertions.assertInstanceOf(Dish.class, dishService.getRandomDish());
    }

    @Test
    public void getRandomDishFromRestaurantOffer(){
        when(dishRepository.findAllByRestaurantName(anyString())).thenReturn(new ArrayList<Dish>());

        Assertions.assertInstanceOf(Dish.class, dishService.getRandomDishFromRestaurantOffer("Test restaurant"));
    }

}
