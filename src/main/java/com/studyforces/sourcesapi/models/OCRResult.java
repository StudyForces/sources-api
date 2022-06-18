package com.studyforces.sourcesapi.models;

import javax.persistence.*;

@Entity
public class OCRResult {
    @Id
    @GeneratedValue
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Embedded
    private OCRRect rect;

    public OCRRect getRect() {
        return rect;
    }

    public void setRect(OCRRect rect) {
        this.rect = rect;
    }

    @Lob
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Enumerated(EnumType.STRING)
    private OCRResultType type;

    public OCRResultType getType() {
        return type;
    }

    public void setType(OCRResultType type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    private OCRResultStatus status;

    public OCRResultStatus getStatus() {
        return status;
    }

    public void setStatus(OCRResultStatus status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "source_upload_id")
    private SourceUpload sourceUpload;

    public SourceUpload getSourceUpload() {
        return sourceUpload;
    }

    public void setSourceUpload(SourceUpload sourceUpload) {
        this.sourceUpload = sourceUpload;
    }
}
