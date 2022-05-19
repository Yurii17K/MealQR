package com.example.mealqr.pojos;

import com.example.mealqr.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.util.Arrays;

@Getter
@SuperBuilder
@Entity(name = "Customers")
@NoArgsConstructor
public class Customer extends User {

    private String allergies;

    public Customer(Integer ID, String name, String lastName, String city, String email, String pass, String allergies) {
        super(ID, name, lastName, city, email, pass, Role.CUSTOMER);
        this.allergies = allergies;
    }

}
