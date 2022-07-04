package com.studyforces.sourcesapi.models;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

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

    @Lob
    private Object metadata;

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
