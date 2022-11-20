package com.example.mealqr.domain;

import com.example.mealqr.domain.enums.Roles;
import com.example.mealqr.web.rest.request.UserSignUpReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;
import java.util.function.UnaryOperator;

import static com.example.mealqr.domain.enums.Roles.CLIENT;
import static com.example.mealqr.domain.enums.Roles.RESTAURANT_MANAGER;

@Getter
@Builder
@With
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "user_id")
    String userId;

    String email;
    String pass;

    String name;
    String lastName;

    String city;

    @Enumerated(EnumType.STRING)
    Roles role;

    public static User of(UserSignUpReq userSignUpReq, UnaryOperator<String> passwordEncoderFunction) {
        return User.builder()//
                .userId(UUID.randomUUID().toString())
                .email(userSignUpReq.getEmail())//
                .pass(passwordEncoderFunction.apply(userSignUpReq.getPass()))//
                .name(userSignUpReq.getName())//
                .lastName(userSignUpReq.getLastName())//
                .city(userSignUpReq.getCity())//
                .role(userSignUpReq.isClient() ? CLIENT : RESTAURANT_MANAGER)//
                .build();
    }
}


