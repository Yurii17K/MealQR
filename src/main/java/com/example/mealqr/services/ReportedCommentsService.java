package com.example.mealqr.services;

import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.ReportedComment;
import com.example.mealqr.repositories.ReportedCommentsRepository;
import com.example.mealqr.rest.request.ReportedCommentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedCommentsService {
    private final ReportedCommentsRepository reportedCommentsRepository;

    @Async
    public void submitReport(ReportedCommentReq reportedCommentReq) {
        ReportedComment reportedComment = ReportedComment.builder()//
                .dishComment(DishComment.builder().dishOpinionId(reportedCommentReq.getCommentId()).build())//
                .reasoning(reportedCommentReq.getReasoning())//
                .userEmail(reportedCommentReq.getUserEmail())//
                .build();
        reportedCommentsRepository.save(reportedComment);
    }
}