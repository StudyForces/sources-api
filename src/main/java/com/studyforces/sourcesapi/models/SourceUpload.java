package com.studyforces.sourcesapi.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SourceUpload {
    @Id
    @GeneratedValue
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Basic
    private String sourceFile;

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    @ElementCollection
    private Set<SourceUploadRect> rects;

    public Set<SourceUploadRect> getRects() {
        return rects;
    }

    public void setRects(Set<SourceUploadRect> rects) {
        this.rects = rects;
    }
}
