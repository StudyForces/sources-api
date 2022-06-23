package com.studyforces.sourcesapi.models;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class ProblemAttachment {
    @Basic
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
