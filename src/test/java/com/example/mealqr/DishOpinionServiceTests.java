package com.example.mealqr;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.pojos.DishRating;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.*;
import com.example.mealqr.services.DishOpinionService;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class DishOpinionServiceTests {
    
    @Mock
    private  DishRatingRepository dishRatingRepository;
    @Mock
    private  DishRepository dishRepository;
    @Mock
    private  RestaurantEmployeeRepository restaurantEmployeeRepository;
    @Mock
    private  SlopeOne slopeOne;

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    @InjectMocks
    private DishOpinionService dishOpinionService;
    
    @Test
    public void shouldReturnEmptyDishes() {
        
        //given
        when(restaurantEmployeeRepository.existsByRestaurantName(anyString())).thenReturn(false);
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        
        //when
        List<Tuple2<Dish, Tuple2<Double, List<String>>>> result = dishOpinionService.getAllDishesInRestaurantConfiguredForUser(testUserEmail, testRestaurantName);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnDishList(){

        //given
        List<Dish> testList= new ArrayList<>();
        testList.add(new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test"));
        Map<Integer, Double> testMap= new HashMap<>();
        testMap.put(1,2.0);
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";

        when(restaurantEmployeeRepository.existsByRestaurantName(testRestaurantName)).thenReturn(true);
        when(dishRepository.findAllByRestaurantName(testRestaurantName)).thenReturn(testList);
        when(slopeOne.slopeOne(eq(testUserEmail),any())).thenReturn(testMap);

        //when
        List<Tuple2<Dish, Tuple2<Double, List<String>>>> result = dishOpinionService.getAllDishesInRestaurantConfiguredForUser(testUserEmail, testRestaurantName);

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldRejectDishRatingDishDoesNotExist(){
        
        //given
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        Integer testRating = 1;

        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.empty());
        
        //when
        Tuple2<Boolean, String> result = dishOpinionService.addOrUpdateRating(testUserEmail, testDishName, testRestaurantName, testRating);
        
        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST);

        assertEquals(expected, result);
    }

    @Test
    public void shouldAddRating(){
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        Integer testRating = 1;

        Dish testDish = new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test");


        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));
        when(dishRatingRepository.findByDishIdAndUserEmail(any(), anyString())).thenReturn(Optional.empty());
        assertEquals(Tuple.of(true, "Added rating to dish " + testDishName + " from " + testRestaurantName), dishOpinionService.addOrUpdateRating(testUserEmail,testDishName,testRestaurantName,testRating));
    }

    @Test
    public void shouldUpdateRating(){
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        Integer testRating = 1;

        Dish testDish = new Dish(1,"Test","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"test");


        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.of(testDish));


        doReturn(Optional.of(new DishRating())).when(dishRatingRepository).findByDishIdAndUserEmail(any(), anyString());
        assertEquals(Tuple.of(true, "Updated rating to dish " + testDishName + " from " + testRestaurantName), dishOpinionService.addOrUpdateRating(testUserEmail,testDishName,testRestaurantName,testRating));
    }

    @Test
    public void shouldRejectDishCommentDishDoesNotExist(){
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        String testComment = "Test comment";

        when(dishRepository.findByDishNameAndRestaurantName(anyString(),anyString())).thenReturn(Optional.empty());
        assertEquals(Tuple.of(false, SUCH_DISH_DOES_NOT_EXIST), dishOpinionService.addOrUpdateComment(testUserEmail,testDishName,testRestaurantName,testComment));
    }
    
    @Test
    public void shouldAddComment() {

        // given
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        String testComment = "Test comment";

        Dish testDish = new Dish(1, "Test", "test", "test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN, "test");

        // when
        when(dishRepository.findByDishNameAndRestaurantName(anyString(), anyString())).thenReturn(Optional.of(testDish));
        when(dishRatingRepository.findByDishIdAndUserEmail(any(), anyString())).thenReturn(Optional.empty());

        // then
        assertEquals(Tuple.of(true, "Added comment to dish " + testDishName + " from " + testRestaurantName), dishOpinionService.addOrUpdateComment(testUserEmail, testDishName, testRestaurantName, testComment));
    }

}
