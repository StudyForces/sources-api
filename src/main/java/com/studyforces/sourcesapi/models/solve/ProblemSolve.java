package com.studyforces.sourcesapi.models.solve;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Embeddable
public class ProblemSolve {
    @Enumerated(EnumType.STRING)
    @Column(name = "solve_type")
    private ProblemSolveType type;
    @ElementCollection
    private List<ProblemSolveVariant> variants;
    @Column(name = "solve_corrent")
    private ProblemSolveVariant correct;
    @Column(name = "solve_formula")
    private String formula;
}
