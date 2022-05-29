package com.example.mealqr.services;

import com.example.mealqr.pojos.ReportedComment;
import com.example.mealqr.repositories.ReportedCommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class ReportedCommentsService {
    private final ReportedCommentsRepository reportedCommentsRepository;

    public boolean submitReport(@NotNull ReportedComment reportedComment) {
        reportedCommentsRepository.save(reportedComment);
        return true;
    }
}
