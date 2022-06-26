package com.studyforces.sourcesapi.models;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "sourceUpload")
    @OrderBy("rect.page, rect.x, rect.y")
    private List<OCRResult> ocrResults;

    public List<OCRResult> getOcrResults() {
        return ocrResults;
    }

    public void setOcrResults(List<OCRResult> ocrResults) {
        this.ocrResults = ocrResults;
    }

    @Lob
    private List<String> convertedFiles;

    public List<String> getConvertedFiles() {
        return this.convertedFiles;
    }

    public void setConvertedFiles(List<String> convertedFiles) {
        this.convertedFiles = convertedFiles;
    }

    @Embedded
    private SourceMetadata metadata;

    public SourceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SourceMetadata metadata) {
        this.metadata = metadata;
    }
}
