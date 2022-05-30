package com.example.mealqr.pojos;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;

@AllArgsConstructor
@Getter
@Builder
@Entity(name = "Dishes")
@NoArgsConstructor
@ToString
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer ID;

    @NotBlank
    @Column(nullable = false)
    private String dishName;

    @NotBlank
    @Column(nullable = false)
    private String restaurantName;

    @NotNull
    @Column(nullable = false)
    private byte[] dishImg;

    @NotNull
    @Column(nullable = false)
    private BigDecimal dishPrice;

    @NotBlank
    @Column(nullable = false)
    private String dishDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish dish = (Dish) o;

        if (!ID.equals(dish.ID)) return false;
        if (!dishName.equals(dish.dishName)) return false;
        if (!restaurantName.equals(dish.restaurantName)) return false;
        if (!Arrays.equals(dishImg, dish.dishImg)) return false;
        if (!dishPrice.equals(dish.dishPrice)) return false;
        return dishDescription.equals(dish.dishDescription);
    }

    @Override
    public int hashCode() {
        int result = ID.hashCode();
        result = 31 * result + dishName.hashCode();
        result = 31 * result + restaurantName.hashCode();
        result = 31 * result + Arrays.hashCode(dishImg);
        result = 31 * result + dishPrice.hashCode();
        result = 31 * result + dishDescription.hashCode();
        return result;
    }
}
