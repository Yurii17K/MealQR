package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.DishImage;
import com.example.mealqr.web.rest.reponse.ImageDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDtoMapper {

    public static ImageDto mapToImageDto(DishImage dishImage) {
        return ImageDto.of(
                decompressImage(dishImage.getData()),
                dishImage.getContentType()
        );
    }

    private static String decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        byte[] output = new byte[0];
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            LoggerFactory.getLogger(ImageDto.class).info("Size before decompression {}", data.length);

            byte[] tmp = new byte[4 * 1024];
            while (!inflater.finished()) {
                int size = inflater.inflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            output = outputStream.toByteArray();

            LoggerFactory.getLogger(ImageDto.class).info("Size after decompression {}", output.length);
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }

        return new String(output);
    }
}
