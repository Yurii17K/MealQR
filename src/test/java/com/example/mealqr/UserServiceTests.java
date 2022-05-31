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
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerAllergyRepository customerAllergyRepository;

    @Mock
    private RestaurantEmployeeRepository restaurantEmployeeRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //without this you will get NPE
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void createUserButItExists() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(new User()));

        Assertions.assertEquals(Tuple.of(false,"User with this email already exists"),userService.signUpCustomer(User.builder().build(), "test"));
    }

    @Test
    public void createUserButUserWasNotAdded() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        Assertions.assertEquals(Tuple.of(false,"Something went wrong when trying to add a user to the database"),userService.signUpCustomer(User.builder().build(), "test"));
    }

    @Test
    public void signUpCustomer() {
        User user = new User(1, "name", "surname", "city", "test@email.com", "pass", Roles.CUSTOMER);

        when(userRepository.findUserByEmail("test@email.com")).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail("test.test@email.com")).thenReturn(Optional.of(user));
        doCallRealMethod().when(userRepository.save(user)).setEmail("test.test@email.com");

        Assertions.assertEquals(Tuple.of(false,"Something went wrong when trying to add a user to the database"),userService.signUpCustomer(user, "test"));
    }

}
