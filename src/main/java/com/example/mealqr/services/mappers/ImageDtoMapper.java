package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.DishImage;
import com.example.mealqr.web.rest.reponse.ImageDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDtoMapper {

    public static ImageDto mapToImageDto(DishImage dishImage) {
        return ImageDto.of(
                decompressImage(dishImage.getData()),
                dishImage.getContentType()
        );
    }

    private static String decompressImage(byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION, true);
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, compressor)) {
            deflaterOutputStream.write(data);
        } catch (IOException e) {
            LoggerFactory.getLogger(ImageDto.class).error("Error while decompressing an image");
        }
        return outputStream.toString();
    }
}
