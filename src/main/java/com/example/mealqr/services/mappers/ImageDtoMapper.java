package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.DishImage;
import com.example.mealqr.web.rest.reponse.ImageDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDtoMapper {

    public static ImageDto mapToImageDto(DishImage dishImage) {
        return ImageDto.of(
                new String(dishImage.getData()),
                dishImage.getContentType()
        );
    }
}
