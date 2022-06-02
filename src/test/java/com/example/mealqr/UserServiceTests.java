package com.example.mealqr;
import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.*;
import com.example.mealqr.security.Roles;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserButItExists() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(new User()));

        assertEquals(Tuple.of(false,"User with this email already exists"),userService.signUpCustomer(User.builder().build(), "test"));
    }

    @Test
    public void createUserButUserWasNotAdded() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        assertEquals(Tuple.of(false,"Something went wrong when trying to add a user to the database"),userService.signUpCustomer(User.builder().build(), "test"));
    }

}
