package com.BTA.SmartPrep.repository;
import com.BTA.SmartPrep.domain.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findByUser_UserIdAndProblemIsNotNullOrderBySubmittedAtDesc(String userId);
}
