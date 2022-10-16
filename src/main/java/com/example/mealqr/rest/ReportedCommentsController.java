package com.example.mealqr.rest;

import com.example.mealqr.rest.request.ReportedCommentReq;
import com.example.mealqr.services.ReportedCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportedCommentsController {
    private final ReportedCommentsService reportedCommentsService;


    @PostMapping("/report-comment")
    @PreAuthorize("hasAuthority(#reportedCommentReq.userEmail)")
    public ResponseEntity<Boolean> submitReport(@RequestBody @Valid ReportedCommentReq reportedCommentReq) {
        reportedCommentsService.submitReport(reportedCommentReq);
        return ResponseEntity.ok(true);
    }
}
