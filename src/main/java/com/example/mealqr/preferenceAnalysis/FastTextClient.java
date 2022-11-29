package com.example.mealqr.preferenceAnalysis;

import org.springframework.stereotype.Component;

import javax.json.Json;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class FastTextClient {

    public Double getTextualSimilarityBetweenDishes(String dish1Name, String dish1Description, String dish2Name, String dish2Description) {
        String jsonString = Json.createObjectBuilder()
                .add("dish1_name", dish1Name)
                .add("dish1_description", dish1Description)
                .add("dish2_name", dish2Name)
                .add("dish2_description", dish2Description)
                .build().toString();
        Double value;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://127.0.0.1:5000/get-dish-similarity"))
                    .setHeader("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .build();
            HttpResponse response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            value = Double.parseDouble(response.body().toString());
        }catch(Exception e){ //Gotta catch'em all
            System.out.println(e);
            value = -1.0;
        }

        return value;
    }
}
