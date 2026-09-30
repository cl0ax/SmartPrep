package com.BTA.SmartPrep.service;

import com.BTA.SmartPrep.domain.dto.problem.SolutionSubmissionDto;
import com.BTA.SmartPrep.domain.dto.submission.SubmissionHistoryDto;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SolutionService {
    SolutionSubmissionDto solutionGrade(String codeString, long problemId, String userId, int categoryId);
    List<SubmissionHistoryDto> getSubmissions(String userId);
    String solutionRunGrade(String codeString,long problemId,String userId,int categoryId);
    Path writeSourceFile(String codeString) throws IOException;
    void compile(Path sourceFile);
    Class<?> loadSolutionClass(Path sourceFile) throws Exception;
    Object execute(Class<?> solutionClass, String methodName, Object... args) throws Exception
            ;
}
