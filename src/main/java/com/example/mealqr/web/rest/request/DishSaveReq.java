package com.example.mealqr.web.rest.request;

import com.example.mealqr.web.rest.reponse.ImageDto;
import lombok.Value;

import javax.validation.Valid;
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
    String restaurantId;
    @NotNull(message = "Dish should have an image")
    @Valid
    ImageDto dishImage;
    @NotNull(message = "Dish should have a price")
    BigDecimal dishPrice;
    @NotBlank
    @Size(max = 1000)
    String dishDescription;
}
