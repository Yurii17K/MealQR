//package com.example.mealqr;
//
//import com.example.mealqr.domain.Dish;
//import com.example.mealqr.repositories.DishRepository;
//import com.example.mealqr.services.DishService;
//import io.vavr.Tuple;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DIshServiceTests {
//
//    @Mock
//    private DishRepository dishRepository;
//
//    @InjectMocks
//    private DishService dishService;
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getsAllDishesInRestaurant() {
//        when(dishRepository.findAllByRestaurantName(anyString())).thenReturn(new ArrayList<>());
//
//        assertEquals(new ArrayList<Dish>(),dishService.getAllDishesInRestaurant("someRestaurant"));
//    }
//
//    @Test
//    public void addDishButItExists(){
//        doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
//
//        assertEquals(Tuple.of(false,"Dish with this name already exists in this restaurant offer"),dishService.addDishToRestaurantMenu(new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
//    }
//
//    @Test
//    public void addDish(){
//        doReturn(Optional.empty()).doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
//        String dishName = "Test name";
//        String restaurantName = "Restaurant name";
//        assertEquals(Tuple.of(true,"Dish " + dishName + " added to " + restaurantName + " offer"),dishService.addDishToRestaurantMenu(new Dish(1,dishName,restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
//
//    }
//
//    @Test
//    public void removeDishButItDoesNotExist(){
//        doReturn(Optional.empty()).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
//        assertEquals(Tuple.of(false, "Dish with this name does not exist in this restaurant offer"),dishService.removeDishFromRestaurantOffer("Test dish", "Test restaurant"));
//
//    }
//
//    @Test
//    public void updateDishButItDoesNotExist(){
//        doReturn(Optional.empty()).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
//        String restaurantName = "Restaurant name";
//
//        assertEquals(Tuple.of(false, "No such dish in " + restaurantName + " offer"),dishService.updateDishInRestaurantOffer(new Dish(1,"Test",restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
//    }
//    @Test
//    public void updateDish(){
//        doReturn(Optional.of(new Dish())).when(dishRepository).findByDishNameAndRestaurantName(anyString(),anyString());
//        String dishName = "Test name";
//        String restaurantName = "Restaurant name";
//        assertEquals(Tuple.of(true, "Dish " + dishName + " data updated in " + restaurantName + " offer"),dishService.updateDishInRestaurantOffer(new Dish(1,dishName,restaurantName,"test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test")));
//    }
//
//    @Test
//    public void getRandomDish(){
//        when(dishRepository.findAll()).thenReturn(new ArrayList<>());
//
//        assertNotNull(dishService.getRandomDish());
//    }
//
//    @Test
//    public void getRandomDishFromRestaurantOffer(){
//        when(dishRepository.findAllByRestaurantName(anyString())).thenReturn(new ArrayList<>());
//
//        assertNotNull(dishService.getRandomDishFromRestaurantOffer("Test restaurant"));
//    }
//
//}
