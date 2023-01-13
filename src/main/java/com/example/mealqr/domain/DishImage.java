package com.example.mealqr.domain;

import com.example.mealqr.web.rest.reponse.ImageDto;
import com.example.mealqr.web.rest.request.DishSaveReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

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
    String dishImageId;

    @Transient
    @OneToOne(targetEntity = Dish.class)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    Dish dish;

    @Lob
    byte[] data;
    String contentType;

    public static DishImage of(DishSaveReq dishSaveReq, String dishId) {
        return DishImage.builder()//
                .dishImageId(UUID.randomUUID().toString())//
                .dish(Dish.builder().dishId(dishId).build())//
                .data(dishSaveReq.getDishImage().getCompressedImage())//
                .contentType(dishSaveReq.getDishImage().getContentType())//
                .build();
    }

    public static DishImage update(ImageDto newImage, DishImage originalImage) {
        return DishImage.builder()//
                .dishImageId(originalImage.getDishImageId())//
                .data(newImage.getCompressedImage())//
                .contentType(newImage.getContentType())//
                .build();
    }
}
