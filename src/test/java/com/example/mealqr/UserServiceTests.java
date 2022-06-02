package com.example.mealqr;
import com.example.mealqr.pojos.CustomerAllergy;
import com.example.mealqr.pojos.RestaurantEmployee;
import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.*;
import com.example.mealqr.security.JWT;
import com.example.mealqr.security.Roles;
import com.example.mealqr.services.UserService;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

    private static MockedStatic<JWT> jwtMockedStatic;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private CustomerAllergyRepository customerAllergyRepository;
    @Mock
    private RestaurantEmployeeRepository restaurantEmployeeRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeClass
    public static void init() {
        jwtMockedStatic = mockStatic(JWT.class);
    }

    @AfterClass
    public static void close() {
        jwtMockedStatic.close();
    }

    @Test
    public void signUpUserButItExists() {
        //given
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(new User()));

        //when
        Tuple2<Boolean, String> result1 = userService.signUpCustomer(new User(), "allergies");
        Tuple2<Boolean, String> result2 = userService.signUpRestaurantEmployee(new User(), "restaurant name");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false,"User with this email already exists");
        assertEquals(expected, result1);
        assertEquals(expected, result2);
    }

    @Test
    public void signUpUserButUserWasNotAdded() {
        //given
        when(userRepository.findUserByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashed password");
        when(userRepository.save(any())).thenReturn(new User());

        //when
        Tuple2<Boolean, String> result1 = userService.signUpCustomer(new User(), "allergies");
        Tuple2<Boolean, String> result2 = userService.signUpRestaurantEmployee(new User(), "restaurant name");

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false,"Something went wrong when trying to add a user to the database");
        assertEquals(expected, result1);
        assertEquals(expected, result2);
    }

    @Test
    public void signUpUserValid() {
        //given
        String allergies = "allergies";
        String restaurantName = "restaurant name";
        User dummyUser = User.builder()
                .name("name")
                .lastName("lastname")
                .city("city")
                .email("email@email.com")
                .pass("pass")
                .role(Roles.CUSTOMER)
                .build();
        CustomerAllergy customerAllergy = new CustomerAllergy()
                .withUserEmail(dummyUser.getEmail())
                .withAllergies(allergies);
        RestaurantEmployee restaurantEmployee = new RestaurantEmployee()
                .withUserEmail(dummyUser.getEmail())
                .withRestaurantName(restaurantName);

        when(userRepository.findUserByEmail(dummyUser.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(dummyUser))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(dummyUser));
        when(userRepository.save(dummyUser)).thenReturn(dummyUser);
        when(bCryptPasswordEncoder.encode(dummyUser.getPass())).thenReturn("hashed password");
        when(customerAllergyRepository.save(customerAllergy)).thenReturn(customerAllergy);
        when(restaurantEmployeeRepository.save(restaurantEmployee)).thenReturn(restaurantEmployee);
        jwtMockedStatic.when(() -> JWT.generateToken(any())).thenReturn("token1.token2.token3");

        //when
        Tuple2<Boolean, String> result1 = userService.signUpCustomer(dummyUser, allergies);
        Tuple2<Boolean, String> result2 = userService.signUpRestaurantEmployee(dummyUser, restaurantName);

        //then
        Tuple2<Boolean, String> expected =
                Tuple.of(true, "token1.token2.token3");
        assertEquals(expected, result1);
        assertEquals(expected, result2);
    }

    @Test
    public void signInCustomerWhenUserNotExist() {
        //given
        String userEmail = "userEmail";
        String userPass = "userPass";
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        //when
        Tuple2<Boolean, String> result = userService.singInUser(userEmail, userPass);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, "Wrong Credentials");
        assertEquals(expected, result);
    }

    @Test
    public void signInCustomerWhenWrongCredentials() {
        //given
        String userEmail = "userEmail";
        String userPass = "userPass";
        User userInDatabase = new User().withPass("wrongPass");
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(userInDatabase));
        when(bCryptPasswordEncoder.matches(userPass, userInDatabase.getPass())).thenReturn(false);

        //when
        Tuple2<Boolean, String> result = userService.singInUser(userEmail, userPass);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, "Wrong Credentials");
        assertEquals(expected, result);
    }

    @Test
    public void signInCustomerValid() {
        //given
        String userEmail = "userEmail";
        String userPass = "userPass";
        User userInDatabase = new User().withPass("correctPass");
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(userInDatabase));
        when(bCryptPasswordEncoder.matches(userPass, userInDatabase.getPass())).thenReturn(true);
        jwtMockedStatic.when(() -> JWT.generateToken(any())).thenReturn("token1.token2.token3");

        //when
        Tuple2<Boolean, String> result = userService.singInUser(userEmail, userPass);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "token1.token2.token3");
        assertEquals(expected, result);
    }

    @Test
    public void updateCustomerAllergiesButUserDoesNotExist() {
        //given
        String userEmail = "userEmail";
        String allergies = "allergies";
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        //when
        Tuple2<Boolean, String> result = userService.updateCustomerAllergies(userEmail, allergies);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(false, "No such customer");
        assertEquals(expected, result);
    }

    @Test
    public void updateCustomerAllergiesValid() {
        //given
        String userEmail = "userEmail";
        String allergies = "allergies";
        User user = new User()
                .withEmail(userEmail)
                .withRole(Roles.CUSTOMER);
        CustomerAllergy customerAllergy = new CustomerAllergy()
                .withUserEmail(userEmail)
                .withAllergies(allergies);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(user));
        when(customerAllergyRepository.save(customerAllergy)).thenReturn(customerAllergy);

        //when
        Tuple2<Boolean, String> result = userService.updateCustomerAllergies(userEmail, allergies);

        //then
        Tuple2<Boolean, String> expected = Tuple.of(true, "Customer allergies are updated");
        assertEquals(expected, result);
    }
}
