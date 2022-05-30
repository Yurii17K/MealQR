package com.example.mealqr.controllers;

import com.example.mealqr.pojos.ReportedComment;
import com.example.mealqr.services.ReportedCommentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportedCommentsController {
    private final ReportedCommentsService reportedCommentsService;


    @PostMapping
    @PreAuthorize("hasAuthority(#userEmail)")
    public ResponseEntity<Void> submitReport(
            @RequestParam String userEmail,
            @RequestParam Integer commentId,
            @RequestParam String reasoning
    ) {
        ReportedComment reportedComment = ReportedComment.builder()
                .userEmail(userEmail)
                .commentId(commentId)
                .reasoning(reasoning)
                .build();

        reportedCommentsService.submitReport(reportedComment);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
