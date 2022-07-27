package com.studyforces.sourcesapi.models.solve;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class ProblemSolveVariant {
    @Column(name = "variant_type")
    private ProblemSolveVariantType type;
    private Float number;
    private String string;
    private Integer index;
}
