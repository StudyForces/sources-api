package com.studyforces.sourcesapi.services.messages.core;

import com.studyforces.sourcesapi.models.ProblemAttachment;
import com.studyforces.sourcesapi.models.ProblemType;
import com.studyforces.sourcesapi.models.solve.ProblemSolve;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProblemSyncMessage {
    private ProblemType type;
    private String problem;
    private String solution;
    private ProblemSolve solverMetadata;
    private Long coreId;
    private Long sourcesId;
    private List<ProblemAttachment> attachments;
}
