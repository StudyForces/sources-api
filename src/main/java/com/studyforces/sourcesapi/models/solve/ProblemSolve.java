package com.studyforces.sourcesapi.models.solve;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Data
@Embeddable
public class ProblemSolve {
    @Column(name = "solve_type")
    private ProblemSolveType type;
    @ElementCollection
    private List<ProblemSolveVariant> variants;
    private ProblemSolveVariant correct;
    private String formula;
}
