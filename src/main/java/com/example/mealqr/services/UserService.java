package com.example.mealqr.services;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.domain.User;
import com.example.mealqr.domain.enums.Roles;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.repositories.CustomerAllergyRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.security.JWT;
import com.example.mealqr.web.rest.request.CustomerAllergiesUpdateReq;
import com.example.mealqr.web.rest.request.UserSignInReq;
import com.example.mealqr.web.rest.request.UserSignUpReq;
import io.vavr.API;
import io.vavr.Function2;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Either<ApiError, String> singInUser(UserSignInReq userSignInReq) {
        return userRepository.findUserByEmail(userSignInReq.getUserEmail())//
                .filter(user -> bCryptPasswordEncoder.matches(userSignInReq.getUserPassword(), user.getPass()))//
                .map(JWT::generateToken)//
                .toEither(ApiError.buildError("Bad credentials!"));
    }

    public Either<Seq<ApiError>, String> signUpUser(UserSignUpReq userSignUpReq) {
        return validateUserSignUp(userSignUpReq)//
                .map(emailIsUnique -> {
                    User user = User.of(userSignUpReq, bCryptPasswordEncoder::encode);
                    if (user.getRole() == Roles.CLIENT) {
                        addAllergies(userSignUpReq);
                    }
                    return JWT.generateToken(userRepository.save(user));
                })//
                .toEither();
    }

    public Either<ApiError, CustomerAllergy> updateCustomerAllergies(String userEmail,
            CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return userRepository.findUserByEmail(userEmail)//
                .map(userPresent -> customerAllergyRepository.save(
                        CustomerAllergy.of(userEmail, customerAllergiesUpdateReq)))//
                .toEither(ApiError.buildError("User with this email does not exist", HttpStatus.NOT_FOUND));
    }

    private void addAllergies(UserSignUpReq userSignUpReq) {
        userSignUpReq.getAllergies()//
                .peek(allergiesPresent -> customerAllergyRepository.save(CustomerAllergy.builder()//
                        .userEmail(userSignUpReq.getEmail())//
                        .allergies(allergiesPresent)//
                        .build()));
    }

    private Validation<Seq<ApiError>, UserSignUpReq> validateUserSignUp(UserSignUpReq userSignUpReq) {
        return Validation.combine(
                        validatePassword(userSignUpReq),
                        validateEmail(userSignUpReq)
                )//
                .ap(Function2.constant(userSignUpReq))//
                .mapError(Function.identity());
    }

    private Validation<ApiError, UserSignUpReq> validatePassword(UserSignUpReq userSignUpReq) {
        return API.Some(userSignUpReq.getPass())//
                .filter(pass -> pass.length() >= 8 && pass.length() <= 128)//
                .map(userNotPresent -> userSignUpReq)//
                .toValidation(ApiError.buildError("Password not secure"));
    }

    private Validation<ApiError, UserSignUpReq> validateEmail(UserSignUpReq userSignUpReq) {
        return API.Some(userRepository.findUserByEmail(userSignUpReq.getEmail()))//
                .filter(Option::isEmpty)//
                .map(userNotPresent -> userSignUpReq)//
                .toValidation(ApiError.buildError("User with this email already exists"));
    }
}
