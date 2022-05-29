package com.example.mealqr.repositories;

import com.example.mealqr.pojos.ReportedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedCommentsRepository extends JpaRepository<ReportedComment, Integer> {

}
