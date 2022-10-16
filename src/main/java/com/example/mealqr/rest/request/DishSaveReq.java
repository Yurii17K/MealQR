package com.example.mealqr.rest.request;

import com.example.mealqr.rest.dto.ImageDto;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Value
public class DishSaveReq {

    @NotBlank
    @Size(max = 128)
    String dishName;
    @NotBlank
    @Size(max = 128)
    String restaurantName;
    @NotNull(message = "Dish should have an image")
    ImageDto dishImage;
    @NotNull(message = "Dish should have a price")
    BigDecimal dishPrice;
    @NotBlank
    @Size(max = 1000)
    String dishDescription;
}
