package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Restaurant {

    @Id
    @Column(name = "restaurant_id")
    String restaurantId;

    String restaurantName;
    String restaurantCity;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "manager_email", referencedColumnName = "email")
    User restaurantManager;

    public static Restaurant of(RestaurantSaveReq restaurantSaveReq, String userEmail) {
        return Restaurant.builder()//
                .restaurantId(UUID.randomUUID().toString())//
                .restaurantName(restaurantSaveReq.getRestaurantName())//
                .restaurantCity(restaurantSaveReq.getRestaurantCity())//
                .restaurantManager(User.builder().email(userEmail).build())//
                .build();
    }
}