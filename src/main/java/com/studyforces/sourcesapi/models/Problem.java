package com.studyforces.sourcesapi.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Problem {

    @Id
    @GeneratedValue
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    private ProblemType type;

    public ProblemType getType() {
        return type;
    }

    public void setType(ProblemType type) {
        this.type = type;
    }

    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String problem;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String solution;

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Lob
    private Object solverMetadata;

    public Object getSolverMetadata() {
        return solverMetadata;
    }

    public void setSolverMetadata(Object solverMetadata) {
        this.solverMetadata = solverMetadata;
    }

    @Basic
    private Long coreId;

    public Long getCoreId() {
        return coreId;
    }

    public void setCoreId(Long coreId) {
        this.coreId = coreId;
    }

    @OneToMany(mappedBy = "problem")
    private Set<OCRResult> ocrResults;

    public Set<OCRResult> getOcrResults() {
        return ocrResults;
    }

    public void setOcrResults(Set<OCRResult> ocrResults) {
        this.ocrResults = ocrResults;
    }
}
