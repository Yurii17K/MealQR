package com.example.mealqr.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ReportedComments")
public class ReportedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reportId;

    @NotNull
    @Column(nullable = false)
    private Integer commentId;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String userEmail;

    @NotBlank
    @Column(nullable = false)
    private String reasoning;
}
