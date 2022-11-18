package com.example.mealqr.repositories;

import com.example.mealqr.domain.ReportedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedCommentsRepository extends JpaRepository<ReportedComment, String> {
}
