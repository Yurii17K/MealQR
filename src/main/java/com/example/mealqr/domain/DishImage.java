package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.DishSaveReq;
import com.example.mealqr.web.rest.request.DishUpdateReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.function.UnaryOperator;

@Entity
@Table(name = "dish_image")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishImage {

    @Id
    @Column(name = "dish_image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer dishImageId;

    @Transient
    @OneToOne(targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    byte[] data;
    String contentType;

    public static DishImage of(DishSaveReq dishSaveReq) {
        return DishImage.builder()//
                .data(dishSaveReq.getDishImage().getBase64Data().getBytes(StandardCharsets.UTF_8))//
                .contentType(dishSaveReq.getDishImage().getContentType())//
                .build();
    }

    public static DishImage of(DishUpdateReq dishUpdateReq, UnaryOperator<DishImage> function) {
        return dishUpdateReq.getDishImage()//
                .map(newImage -> function.apply(DishImage.builder()//
                            .data(newImage.getBase64Data().getBytes(StandardCharsets.UTF_8))//
                            .contentType(newImage.getContentType())//
                            .build()))//
                .getOrElse(DishImage.builder().build());
    }
}
