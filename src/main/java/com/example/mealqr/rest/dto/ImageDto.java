package com.example.mealqr.rest.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class ImageDto {
    String base64Data;
    String contentType;
}
