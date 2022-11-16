package com.example.mealqr.domain;

import com.example.mealqr.security.Roles;
import com.example.mealqr.web.rest.request.UserSignUpReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.function.UnaryOperator;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    Integer userId;

    String email;
    String pass;

    String name;
    String lastName;

    String city;

    @Enumerated(EnumType.STRING)
    Roles role;

    public static User of(UserSignUpReq userSignUpReq, UnaryOperator<String> passwordEncoderFunction) {
        return User.builder()//
                .email(userSignUpReq.getEmail())//
                .pass(passwordEncoderFunction.apply(userSignUpReq.getPass()))//
                .name(userSignUpReq.getName())//
                .lastName(userSignUpReq.getLastName())//
                .city(userSignUpReq.getCity())//
                .role(userSignUpReq.isClient() ? Roles.CUSTOMER : Roles.RESTAURANT_MANAGER)//
                .build();
    }
}


