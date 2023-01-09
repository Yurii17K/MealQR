package com.example.mealqr.web.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;
import lombok.Value;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UserSignUpReq {

    boolean client;

    @NotBlank(message = "User name should not be empty")
    @Size(max = 128)
    String name;

    @NotBlank(message = "User last name should not be empty")
    @Size(max = 128)
    String lastName;

    @NotBlank(message = "User city should not be empty")
    @Size(max = 128)
    String city;

    @Nullable
    @Size(max = 512)
    @JsonProperty
    String allergies;

    @Email(message = "User email should be valid", regexp = ".*@.*\\..*")
    String email;

    // passwords visible in logs at failed validation if below is applied
    @Size(min = 12, max = 128, message = "User password lengths can not be less than 12")
    String pass;

    @JsonIgnore
    public Option<String> getAllergies() {
        return Option.of(allergies);
    }
}
