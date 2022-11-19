package com.example.mealqr.web.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CustomerAllergiesUpdateReq {

    @NotBlank
    @JsonProperty
    String allergies;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static CustomerAllergiesUpdateReq of(String allergies) {
        return new CustomerAllergiesUpdateReq(allergies);
    }
}
