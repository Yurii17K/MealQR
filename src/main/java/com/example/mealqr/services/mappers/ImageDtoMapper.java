package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.DishImage;
import com.example.mealqr.web.rest.reponse.ImageRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDtoMapper {

    public static ImageRes mapToImageDto(DishImage dishImage) {
        return ImageRes.of(
                new String(dishImage.getData()),
                dishImage.getContentType()
        );
    }
}
