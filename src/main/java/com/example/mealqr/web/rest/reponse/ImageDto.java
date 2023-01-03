package com.example.mealqr.web.rest.reponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@Value(staticConstructor = "of")
public class ImageDto {
    String base64Data;
    String contentType;

    @JsonIgnore
    public byte[] getBase64Data() {
        return compressImage(base64Data);
    }

    @JsonIgnore
    private byte[] compressImage(String base64Data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION, true);
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, compressor)) {
            deflaterOutputStream.write(base64Data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerFactory.getLogger(ImageDto.class).error("Error while converting an image base64 string into byte array");
        }
        return outputStream.toByteArray();
    }
}
