package com.example.mealqr.services;

import com.example.mealqr.domain.ReportedComment;
import com.example.mealqr.repositories.ReportedCommentsRepository;
import com.example.mealqr.web.rest.request.ReportedCommentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedCommentsService {
    private final ReportedCommentsRepository reportedCommentsRepository;

    @Async
    public void submitReport(String userEmail, ReportedCommentReq reportedCommentReq) {
        ReportedComment reportedComment = ReportedComment.of(userEmail, reportedCommentReq);
        reportedCommentsRepository.save(reportedComment);
    }
}