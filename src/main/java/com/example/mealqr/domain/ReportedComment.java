package com.example.mealqr.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reported_comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_id")
    Integer reportId;

    @ManyToOne(targetEntity = DishComment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "dish_opinion_id")
    DishComment dishComment;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    String userEmail;

    String reasoning;
}