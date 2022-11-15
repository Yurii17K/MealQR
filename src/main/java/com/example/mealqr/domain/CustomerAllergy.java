package com.example.mealqr.domain;

import com.example.mealqr.rest.request.CustomerAllergiesUpdateReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@With
@Entity
@Table(name = "customer_allergies")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerAllergy {

    @Id
    @Column(name = "user_email")
    String userEmail;
    String allergies;

    public static CustomerAllergy of(String userEmail, CustomerAllergiesUpdateReq customerAllergiesUpdateReq) {
        return CustomerAllergy.builder()//
                .userEmail(userEmail)//
                .allergies(customerAllergiesUpdateReq.getAllergies())//
                .build();
    }
}
