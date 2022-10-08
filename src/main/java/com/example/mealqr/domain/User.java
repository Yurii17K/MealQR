package com.example.mealqr.domain;

import com.example.mealqr.security.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Builder
@With
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    Integer userId;

    String email;
    String pass;

    String name;
    String lastName;

    String city;

    @Enumerated(EnumType.STRING)
    Roles role;
}


