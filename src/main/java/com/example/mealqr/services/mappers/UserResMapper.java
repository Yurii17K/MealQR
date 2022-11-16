package com.example.mealqr.services.mappers;


import com.example.mealqr.domain.User;
import com.example.mealqr.web.rest.reponse.UserRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResMapper {

    public static UserRes mapToUserRes(User user) {
        return UserRes.of(
                user.getUserId().toString(),
                user.getEmail(),
                user.getName(),
                user.getLastName(),
                user.getCity(),
                user.getRole()
        );
    }
}
