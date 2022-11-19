package com.example.mealqr.web.rest.request;

import com.example.mealqr.web.rest.reponse.ImageDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;
import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Value
public class DishUpdateReq {

    @NotBlank
    String dishId;

    @NotBlank
    String restaurantId;

    @Nullable
    @Size(max = 200)
    @JsonProperty
    String dishName;

    @Nullable
    @JsonProperty
    ImageDto dishImage;

    @Nullable
    @JsonProperty
    BigDecimal dishPrice;

    @Nullable
    @Size(max = 1000)
    @JsonProperty
    String dishDescription;


    @JsonIgnore
    public Option<String> getDishName() {
        return Option.of(dishName);
    }

    @JsonIgnore
    public Option<ImageDto> getDishImage() {
        return Option.of(dishImage);
    }

    @JsonIgnore
    public Option<BigDecimal> getDishPrice() {
        return Option.of(dishPrice);
    }

    @JsonIgnore
    public Option<String> getDishDescription() {
        return Option.of(dishDescription);
    }
}
