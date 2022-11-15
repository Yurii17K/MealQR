package com.example.mealqr.rest.request;

import com.example.mealqr.rest.dto.ImageDto;
import io.vavr.control.Option;
import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Value
public class DishUpdateReq {

    @NotNull
    int dishId;

    @Nullable
    @Size(max = 200)
    String dishName;

    @NotBlank
    @Size(max = 200)
    String restaurantId;

    @Nullable
    ImageDto dishImage;

    @Nullable
    BigDecimal dishPrice;

    @Nullable
    @Size(max = 1000)
    String dishDescription;

    public Option<String> getDishName() {
        return Option.of(dishName);
    }

    public Option<ImageDto> getDishImage() {
        return Option.of(dishImage);
    }

    public Option<BigDecimal> getDishPrice() {
        return Option.of(dishPrice);
    }

    public Option<String> getDishDescription() {
        return Option.of(dishDescription);
    }
}
