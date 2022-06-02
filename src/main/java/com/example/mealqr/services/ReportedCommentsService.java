package com.example.mealqr.services;

import com.example.mealqr.pojos.ReportedComment;
import com.example.mealqr.repositories.ReportedCommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class ReportedCommentsService {
    private final ReportedCommentsRepository reportedCommentsRepository;

    @Async
    public void submitReport(@NotNull ReportedComment reportedComment) {
        reportedCommentsRepository.save(reportedComment);
    }
}