package com.studyforces.sourcesapi.requests;

import com.studyforces.sourcesapi.models.ProblemAttachment;
import com.studyforces.sourcesapi.models.ProblemType;
import com.studyforces.sourcesapi.models.solve.ProblemSolve;
import lombok.Data;

import java.util.List;

@Data
public class ProblemUpdateRequest {
    private ProblemType type;
    private String problem;
    private String solution;
    private ProblemSolve solverMetadata;
    private List<Long> ocrResults;
    private List<ProblemAttachment> attachments;
}
