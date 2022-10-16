package com.example.mealqr.services;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.domain.User;
import com.example.mealqr.repositories.CustomerAllergyRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.rest.request.CustomerAllergiesUpdateReq;
import com.example.mealqr.rest.request.UserSignInReq;
import com.example.mealqr.rest.request.UserSignUpReq;
import com.example.mealqr.security.JWT;
import com.example.mealqr.security.Roles;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Either<String, String> singInUser(UserSignInReq userSignInReq) {
        return userRepository.findUserByEmail(userSignInReq.getUserEmail())//
                .filter(user -> bCryptPasswordEncoder.matches(userSignInReq.getUserPassword(), user.getPass()))//
                .map(JWT::generateToken)//
                .toEither("Wrong credentials!");
    }

    public Either<String, String> signUpUser(UserSignUpReq userSignUpReq) {
        return validateUserSignUp(userSignUpReq)//
                .peek(this::addAllergies)//
                .map(emailIsUnique -> {
                    User user = User.builder()//
                            .email(userSignUpReq.getEmail())//
                            .pass(bCryptPasswordEncoder.encode(userSignUpReq.getPass()))//
                            .name(userSignUpReq.getName())//
                            .lastName(userSignUpReq.getLastName())//
                            .city(userSignUpReq.getCity())//
                            .role(userSignUpReq.isClient() ? Roles.CUSTOMER : Roles.RESTAURANT_MANAGER)//
                            .build();
                    return JWT.generateToken(userRepository.save(user));
                })//
                .toEither();
    }

    private void addAllergies(UserSignUpReq userSignUpReq) {
        userSignUpReq.getAllergies()//
                .peek(allergiesPresent -> customerAllergyRepository.save(CustomerAllergy.builder()//
                        .userEmail(userSignUpReq.getEmail())//
                        .allergies(allergiesPresent)//
                        .build()));
    }

    private Validation<String, UserSignUpReq> validateUserSignUp(UserSignUpReq userSignUpReq) {
        return API.Some(userRepository.findUserByEmail(userSignUpReq.getEmail()))//
                .filter(Option::isEmpty)//
                .map(userNotPresent -> userSignUpReq)//
                .toValidation("User with this email already exists");
    }

    public Either<String, CustomerAllergy> updateCustomerAllergies(CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return userRepository.findUserByEmail(customerAllergiesUpdateReq.getUserEmail())//
                .map(userPresent -> customerAllergyRepository.save(CustomerAllergy.builder()//
                        .userEmail(customerAllergiesUpdateReq.getUserEmail())//
                        .allergies(customerAllergiesUpdateReq.getAllergies())//
                        .build()))//
                .toEither("User with this email does not exist");
    }
}
