package com.example.mealqr.domain;

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
    @JoinColumn(name = "restaurant_manager_id", referencedColumnName = "user_id")
    User restaurantManager;

    @PrePersist
    void setRestaurantId() {
        this.restaurantId = UUID.randomUUID().toString();
    }
}