package com.example.mealqr.pojos;

import com.example.mealqr.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@SuperBuilder
@Entity(name = "Users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;
    
    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    private String city;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8)
    private String pass;

    @NotBlank
    private Role role;

    public User(Integer ID, String name, String lastName, String city, String email, String pass, Role role) {
        this.ID = ID;
        this.name = name;
        this.lastName = lastName;
        this.city = city;
        this.email = email;
        this.pass = pass;
        this.role = role;
    }
}


