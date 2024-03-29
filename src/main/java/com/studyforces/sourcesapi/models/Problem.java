package com.studyforces.sourcesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.studyforces.sourcesapi.models.solve.ProblemSolve;
import com.studyforces.sourcesapi.support.JpaConverterJson;

import javax.persistence.*;
import java.util.List;

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

    @Embedded
    private ProblemSolve solverMetadata;

    public ProblemSolve getSolverMetadata() {
        return solverMetadata;
    }

    public void setSolverMetadata(ProblemSolve solverMetadata) {
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
    @OrderBy("rect.page, rect.y, rect.x")
    private List<OCRResult> ocrResults;

    @JsonIgnore
    public List<OCRResult> getOcrResults() {
        return ocrResults;
    }

    public void setOcrResults(List<OCRResult> ocrResults) {
        this.ocrResults = ocrResults;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ProblemAttachment> attachments;

    public List<ProblemAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ProblemAttachment> attachments) {
        this.attachments = attachments;
    }
}
