package com.studyforces.sourcesapi.models;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class UploadConvertedFile {
    private String file;
    private Long page;
}
