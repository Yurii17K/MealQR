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

    @NotBlank
    @Size(max = 128)
    String name;

    @NotBlank
    @Size(max = 128)
    String lastName;

    @NotBlank
    @Size(max = 128)
    String city;

    @Nullable
    @Size(max = 512)
    @JsonProperty
    String allergies;

    @NotBlank
    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    String email;

    @NotBlank
    // passwords visible in logs at failed validation if below is applied
    // @Size(min = 8, max = 128, message = "Password lengths can not be less than 8")
    String pass;

    @JsonIgnore
    public Option<String> getAllergies() {
        return Option.of(allergies);
    }
}
