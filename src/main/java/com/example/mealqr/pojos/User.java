package com.example.mealqr.pojos;

import com.example.mealqr.security.Roles;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Builder
@Entity(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer ID;
    
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @Email(message = "Email should be valid", regexp =
    ".*@.*\\..*")
    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String pass;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles role;

}


