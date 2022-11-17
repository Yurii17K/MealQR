package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

//    @ManyToOne(targetEntity = User.class)
//    @JoinColumn(name = "restaurant_manager_id", referencedColumnName = "user_id")
//    User restaurantManager;

//    @PrePersist
//    void setRestaurantId() {
//        this.restaurantId = UUID.randomUUID().toString();
//    }

    public static Restaurant of(RestaurantSaveReq restaurantSaveReq) {
        return Restaurant.builder()
                .restaurantId(UUID.randomUUID().toString())
                .restaurantName(restaurantSaveReq.getRestaurantName())
                .restaurantCity(restaurantSaveReq.getRestaurantCity())
                .build();
    }
}