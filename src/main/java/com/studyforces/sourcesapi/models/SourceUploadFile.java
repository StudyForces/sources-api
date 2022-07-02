package com.studyforces.sourcesapi.models;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class SourceUploadFile {
    private String file;
    private Integer order;
}
