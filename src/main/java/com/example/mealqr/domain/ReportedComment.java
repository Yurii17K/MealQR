package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.ReportedCommentReq;
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
    @Column(name = "report_id")
    String reportId;

    @ManyToOne(targetEntity = DishComment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "dish_opinion_id")
    DishComment dishComment;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    String userEmail;

    String reasoning;

    public static ReportedComment of(String userEmail, ReportedCommentReq reportedCommentReq) {
        return ReportedComment.builder()//
                .dishComment(DishComment.builder().dishOpinionId(reportedCommentReq.getCommentId()).build())//
                .reasoning(reportedCommentReq.getReasoning())//
                .userEmail(userEmail)//
                .build();
    }
}
