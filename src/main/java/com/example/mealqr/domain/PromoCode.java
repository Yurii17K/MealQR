package com.example.mealqr.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Builder
@With
@Entity
@Table(name = "promo_codes")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "promo_code_id")
    Integer promo_code_id;

    Boolean used;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    BigDecimal priceReduction;

    String promoCodeString;
}