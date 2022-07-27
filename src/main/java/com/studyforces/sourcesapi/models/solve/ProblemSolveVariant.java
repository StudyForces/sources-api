package com.studyforces.sourcesapi.models.solve;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class ProblemSolveVariant {
    @Enumerated(EnumType.STRING)
    @Column(name = "variant_type")
    private ProblemSolveVariantType type;
    @Column(name = "variant_number")
    private Float number;
    @Column(name = "variant_string")
    private String string;
    @Column(name = "variant_index")
    private Integer index;
}
