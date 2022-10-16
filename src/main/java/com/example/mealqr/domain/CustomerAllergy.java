package com.example.mealqr.domain;

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
}
