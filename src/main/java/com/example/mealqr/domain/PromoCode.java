package com.example.mealqr.domain;


import com.example.mealqr.domain.enums.PromoCodeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Builder
@Setter
@Entity
@Table(name = "promo_codes")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "promo_code_id")
    Integer promoCodeId;

    String promoCodeString;

    boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurant_id")
    Restaurant restaurant;

    BigDecimal priceReduction;

    PromoCodeType promoCodeType;

}