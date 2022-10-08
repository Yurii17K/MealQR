package com.example.mealqr.rest;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.rest.request.CustomerAllergiesUpdateReq;
import com.example.mealqr.rest.request.UserSignInReq;
import com.example.mealqr.rest.request.UserSignUpReq;
import com.example.mealqr.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/sing-in")
    public ResponseEntity<String> signInUser(@RequestBody @Valid UserSignInReq userSignInReq) {
        return userService.singInUser(userSignInReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PostMapping(value = "/users/sing-up")
    public ResponseEntity<String> signUpCustomer(@RequestBody @Valid UserSignUpReq userSignUpReq) {
        return userService.signUpUser(userSignUpReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PatchMapping("/users/update-allergies")
    @PreAuthorize("hasAuthority({#customerAllergiesUpdateReq.userEmail})")
    @Operation(summary = "changeCustomerAllergies", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<CustomerAllergy> changeCustomerAllergies(
            @RequestBody @Valid CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return userService.updateCustomerAllergies(customerAllergiesUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }
}
