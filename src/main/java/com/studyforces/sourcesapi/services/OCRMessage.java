package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.SourceUploadRect;

public class OCRMessage {
    private Long sourceUploadID;

    public Long getSourceUploadID() {
        return sourceUploadID;
    }

    public void setSourceUploadID(Long sourceUploadID) {
        this.sourceUploadID = sourceUploadID;
    }

    private String sourceUploadURL;

    public String getSourceUploadURL() {
        return sourceUploadURL;
    }

    public void setSourceUploadURL(String sourceUploadURL) {
        this.sourceUploadURL = sourceUploadURL;
    }

    private SourceUploadRect rect;

    public SourceUploadRect getRect() {
        return rect;
    }

    public void setRect(SourceUploadRect rect) {
        this.rect = rect;
    }
}
