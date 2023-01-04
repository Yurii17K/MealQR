package com.example.mealqr.web.rest;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.UserService;
import com.example.mealqr.web.rest.reponse.TokenRes;
import com.example.mealqr.web.rest.request.ChangeUserPasswordReq;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenRes> signInUser(@RequestBody @Valid UserSignInReq userSignInReq) {
        return userService.signInUser(userSignInReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<TokenRes> signUpCustomer(@RequestBody @Valid UserSignUpReq userSignUpReq) {
        return userService.signUpUser(userSignUpReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PutMapping("/update-allergies")
    @Operation(summary = "updateCustomerAllergies", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<CustomerAllergy> updateCustomerAllergies(Principal principal,
            @RequestBody @Valid CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return userService.updateCustomerAllergies(principal.getName(), customerAllergiesUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PutMapping("/change-password")
    @Operation(summary = "changeUserPassword", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> changeUserPassword(Principal principal,
            @RequestBody @Valid ChangeUserPasswordReq changeUserPasswordReq) {
        return userService.changeUserPassword(principal.getName(), changeUserPasswordReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

}
