package com.example.mealqr;

import com.example.mealqr.preferenceAnalysis.SlopeOne;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MealQrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealQrApplication.class, args);
    }

}
