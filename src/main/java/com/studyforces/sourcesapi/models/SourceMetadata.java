package com.studyforces.sourcesapi.models;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class SourceMetadata {

    private Long pages;
    private String type;

}
