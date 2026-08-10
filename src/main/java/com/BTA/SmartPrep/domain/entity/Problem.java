package com.BTA.SmartPrep.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="Problems")
public class Problem {
    public Problem() {
    }

    public Problem(long problemId, String title, String prompt, String examples, ProblemDifficulty problemDifficulty, int category, String starterCode
    ,String sampleTestCase, String methodName, String returnType,String parameterType, String expectedOutput) {
        this.problemId = problemId;
        this.title = title;
        this.prompt = prompt;
        this.examples = examples;
        this.problemDifficulty = problemDifficulty;
        this.category = category;
        this.starterCode = starterCode;
        this.sampleTestCase = sampleTestCase;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterType = parameterType;
        this.expectedOutput = expectedOutput;
    }

    @Id //This Annotation Says this is an ID
    @Column(name = "problem_ID",updatable = false,nullable = false) //col name from DB, not Updatable, cannot be null
    private long problemId;

    @Column(name = "pTitle")
    private String title;

    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "examples", columnDefinition = "TEXT")
    private String examples;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private ProblemDifficulty problemDifficulty;

    @Column(name = "category_ID")
    private int category;

    @Column(name = "starterCode", columnDefinition = "TEXT")
    private String starterCode;


    @Column(name = "sampleTestCase", columnDefinition = "TEXT")
    private String sampleTestCase;

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    @Column(name = "sampleExpectedOutput")
    private String expectedOutput;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Column(name = "methodName")
    private String methodName;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Column(name = "returnType")
    private String returnType;

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    @Column(name = "parameterType")
    private String parameterType;

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public ProblemDifficulty getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(ProblemDifficulty problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public String getSampleTestCase() {
        return sampleTestCase;
    }

    public void setSampleTestCase(String sampleTestCase) {
        this.sampleTestCase = sampleTestCase;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return Objects.equals(problemId, problem.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(problemId);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "problemId=" + problemId +
                ", title='" + title + '\'' +
                ", prompt='" + prompt + '\'' +
                ", examples='" + examples + '\'' +
                ", problemDifficulty=" + problemDifficulty +
                ", category=" + category +
                '}';
    }
}
