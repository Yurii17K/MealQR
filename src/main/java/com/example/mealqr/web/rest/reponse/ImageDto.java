package com.example.mealqr.web.rest.reponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

@Value(staticConstructor = "of")
public class ImageDto {
    // Higher due adjust for conversions (original image will be compressed to 50kb in case of 60kb)
    @Size(max = 65000, message = "Image must be less than 50Kb")
    String base64Data;
    String contentType;

    @JsonIgnore
    public byte[] getCompressedImage() {
        byte[] base64DataBytes = base64Data.getBytes(StandardCharsets.UTF_8);
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(base64DataBytes);
        deflater.finish();

        byte[] output = new byte[0];
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(base64DataBytes.length)) {
            LoggerFactory.getLogger(ImageDto.class).info("Size before compression {}", base64DataBytes.length);

            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            output = outputStream.toByteArray();

            LoggerFactory.getLogger(ImageDto.class).info("Size after compression {}", output.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
}
