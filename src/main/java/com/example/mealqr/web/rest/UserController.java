package com.example.mealqr.web.rest;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.UserService;
import com.example.mealqr.web.rest.request.CustomerAllergiesUpdateReq;
import com.example.mealqr.web.rest.request.UserSignInReq;
import com.example.mealqr.web.rest.request.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/sing-in")
    public ResponseEntity<String> signInUser(@RequestBody @Valid UserSignInReq userSignInReq) {
        return userService.singInUser(userSignInReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PostMapping(value = "/users/sing-up")
    public ResponseEntity<String> signUpCustomer(@RequestBody @Valid UserSignUpReq userSignUpReq) {
        return userService.signUpUser(userSignUpReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PatchMapping("/users/update-allergies")
    @Operation(summary = "changeCustomerAllergies", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<CustomerAllergy> changeCustomerAllergies(Principal principal,
            @RequestBody @Valid CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return userService.updateCustomerAllergies(principal.getName(), customerAllergiesUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
