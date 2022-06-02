package com.example.mealqr;
import com.example.mealqr.pojos.CustomerAllergy;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.pojos.DishComment;
import com.example.mealqr.pojos.DishRating;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.*;
import com.example.mealqr.services.DishOpinionService;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DishOpinionServiceTests {
    
    @Mock
    private  DishRatingRepository dishRatingRepository;
    @Mock
    private  DishRepository dishRepository;
    @Mock
    private  UserRepository userRepository;

    @Mock
    private DishCommentRepository dishCommentRepository;
    @Mock
    private  RestaurantEmployeeRepository restaurantEmployeeRepository;
    @Mock
    private  SlopeOne slopeOne;

    @Mock
    private CustomerAllergyRepository customerAllergyRepository;

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    @InjectMocks
    private DishOpinionService dishOpinionService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

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
        List<String> userEmailList = new ArrayList();
        userEmailList.add(testUserEmail);
        userEmailList.add("test@test.com");
        when(restaurantEmployeeRepository.existsByRestaurantName(testRestaurantName)).thenReturn(true);
        when(dishRepository.findAllByRestaurantName(testRestaurantName)).thenReturn(testList);
        when(slopeOne.slopeOne(eq(testUserEmail),any())).thenReturn(testMap);

        when(userRepository.findAllCustomersWhoRatedAnythingInRestaurant(anyString())).thenReturn(userEmailList);
        //when
        List<Tuple2<Dish, Tuple2<Double, List<String>>>> result = dishOpinionService.getAllDishesInRestaurantConfiguredForUser(testUserEmail, testRestaurantName);

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldReturnListWithoutKiwi(){

        //given
        List<Dish> testList= new ArrayList<>();
        Dish badDish = new Dish(1,"Kiwi kiwi","test","test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN,"Kiwi kiwi kiwi kiwi");
        Dish goodDish = Dish.builder().ID(2).dishName("Good dish").restaurantName("test").dishImg("test".getBytes(StandardCharsets.UTF_8)).dishPrice(BigDecimal.TEN).dishDescription("This dish is very good").build();
        testList.add(badDish);
        testList.add(goodDish);
        Map<Integer, Double> testMap= new HashMap<>();
        testMap.put(1,2.0);
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        CustomerAllergy testAllergy = CustomerAllergy.builder().allergies("kiwi").userEmail(testUserEmail).build();

        when(restaurantEmployeeRepository.existsByRestaurantName(testRestaurantName)).thenReturn(true);
        when(dishRepository.findAllByRestaurantName(testRestaurantName)).thenReturn(testList);
        when(slopeOne.slopeOne(eq(testUserEmail),any())).thenReturn(testMap);
        when(customerAllergyRepository.findByUserEmail(anyString())).thenReturn(Optional.of(testAllergy));
        //when
        List<Tuple2<Dish, Tuple2<Double, List<String>>>> result = dishOpinionService.getAllDishesInRestaurantConfiguredForUser(testUserEmail, testRestaurantName);

        //then
        for(Tuple2<Dish, Tuple2<Double, List<String>>> element : result){
            assertFalse(element._1.getDishDescription().toLowerCase(Locale.ROOT).contains("kiwi"));
        }
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

        
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        String testComment = "Test comment";

        Dish testDish = new Dish(1, "Test", "test", "test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN, "test");

        
        when(dishRepository.findByDishNameAndRestaurantName(anyString(), anyString())).thenReturn(Optional.of(testDish));
        when(dishCommentRepository.findByDishIdAndUserEmail(anyInt(),anyString())).thenReturn(Optional.empty());
        
        assertEquals(Tuple.of(true, "Added comment to dish " + testDishName + " from " + testRestaurantName), dishOpinionService.addOrUpdateComment(testUserEmail, testDishName, testRestaurantName, testComment));
    }
    @Test
    public void shouldUpdateComment() {

        
        String testRestaurantName = "Test restaurant";
        String testUserEmail = "TestUser@email.com";
        String testDishName = "Test dish";
        String testComment = "Test comment";

        Dish testDish = new Dish(1, "Test", "test", "test".getBytes(StandardCharsets.UTF_8), BigDecimal.TEN, "test");
        DishComment testDishComment = DishComment.builder().comment("comment").dishId(1).userEmail(testUserEmail).ID(1).build();
        
        when(dishRepository.findByDishNameAndRestaurantName(anyString(), anyString())).thenReturn(Optional.of(testDish));
        when(dishCommentRepository.findByDishIdAndUserEmail(anyInt(),anyString())).thenReturn(Optional.of(testDishComment));

        
        assertEquals(Tuple.of(true, "Updated comment to dish " + testDishName + " from " + testRestaurantName), dishOpinionService.addOrUpdateComment(testUserEmail, testDishName, testRestaurantName, testComment));
    }
}
