package com.example.mealqr.web.rest.reponse;

import lombok.Value;

@Value(staticConstructor = "of")
public class ImageRes {
    String base64Data;
    String contentType;
}
