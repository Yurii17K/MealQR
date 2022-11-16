package com.example.mealqr.web.rest;

import com.example.mealqr.services.ReportedCommentsService;
import com.example.mealqr.web.rest.request.ReportedCommentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportedCommentsController {
    private final ReportedCommentsService reportedCommentsService;


    @PostMapping("/report-comment")
    public ResponseEntity<Boolean> submitReport(Principal principal,
            @RequestBody @Valid ReportedCommentReq reportedCommentReq) {
        reportedCommentsService.submitReport(principal.getName(), reportedCommentReq);
        return ResponseEntity.ok(true);
    }
}
