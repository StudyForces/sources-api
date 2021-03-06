package com.studyforces.sourcesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy("idx")
    private List<SourceUploadFile> sourceFiles;

    public List<SourceUploadFile> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<SourceUploadFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    @OneToMany(mappedBy = "sourceUpload", cascade = CascadeType.ALL)
    @OrderBy("rect.page, rect.y, rect.x")
    private List<OCRResult> ocrResults;

    @JsonIgnore
    public List<OCRResult> getOcrResults() {
        return ocrResults;
    }

    public void setOcrResults(List<OCRResult> ocrResults) {
        this.ocrResults = ocrResults;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy("page")
    private List<UploadConvertedFile> convertedFiles;

    public List<UploadConvertedFile> getConvertedFiles() {
        return this.convertedFiles;
    }

    public void setConvertedFiles(List<UploadConvertedFile> convertedFiles) {
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
