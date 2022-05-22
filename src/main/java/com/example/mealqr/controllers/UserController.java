package com.example.mealqr.controllers;

import com.example.mealqr.pojos.User;
import com.example.mealqr.security.Roles;
import com.example.mealqr.services.UserService;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Tuple2<Boolean, String>> signInUser(
            @RequestParam String userEmail,
            @RequestParam String userPass
    ) {
        return createResponseBasedOnResult(userService.singInUser(userEmail, userPass));
    }

    @PostMapping(value = "/customer")
    public ResponseEntity<Tuple2<Boolean, String>> signUpCustomer(
            @RequestParam String userName,
            @RequestParam String userLastName,
            @RequestParam String userCity,
            @RequestParam String allergies,
            @RequestParam String userEmail,
            @RequestParam String userPass) {

        User user = User.builder()
                .name(userName)
                .lastName(userLastName)
                .city(userCity)
                .email(userEmail)
                .pass(userPass)
                .role(Roles.CUSTOMER)
                .build();

        Tuple2<Boolean, String> serviceResult = userService.signUpCustomer(user, allergies);

        return createResponseBasedOnResult(serviceResult);

    }

    @PostMapping("/rest-emp")
    public ResponseEntity<Tuple2<Boolean, String>> signUpRestaurantEmployee(
            @RequestParam String userName,
            @RequestParam String userLastName,
            @RequestParam String userCity,
            @RequestParam String restaurantName,
            @RequestParam String userEmail,
            @RequestParam String userPass) {

        User user = User.builder()
                .name(userName)
                .lastName(userLastName)
                .city(userCity)
                .email(userEmail)
                .pass(userPass)
                .role(Roles.RESTAURANT_EMPLOYEE)
                .build();

        Tuple2<Boolean, String> serviceResult = userService.signUpRestaurantEmployee(user, restaurantName);

        return createResponseBasedOnResult(serviceResult);
    }

    @PatchMapping("/customer/allergies")
    public ResponseEntity<Tuple2<Boolean, String>> changeCustomerAllergies(
            @RequestParam String userEmail,
            @RequestParam String allergies
    ) {
        return ResponseEntity.ok()
                .body(userService.updateCustomerAllergies(userEmail, allergies));
    }

    private ResponseEntity<Tuple2<Boolean, String>> createResponseBasedOnResult(Tuple2<Boolean, String> serviceResult) {
        if (!serviceResult._1) {
            return ResponseEntity.ok()
                    .body(serviceResult);
        } else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, serviceResult._2)
                    .body(serviceResult);
        }
    }
}
