package com.example.mealqr.pojos;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
    private double dishPrice;

    @NotBlank
    @Column(nullable = false)
    private String dishDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dish dish = (Dish) o;
        return ID != null && Objects.equals(ID, dish.ID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
