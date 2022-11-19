package com.example.mealqr.web.rest.reponse;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserRes {
    String userId;
    String email;
    String name;
    String lastName;
    String city;
}
