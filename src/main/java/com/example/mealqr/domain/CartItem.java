package com.example.mealqr.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@With
public class CartItem {

    @Id
    @Column(name = "cart_item_id")
    String cartItemId;

    String userEmail;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    int dishQuantity;
    BigDecimal cartItemCost;
}
