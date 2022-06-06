package com.studyforces.sourcesapi.services.messages;

import com.studyforces.sourcesapi.models.SourceUploadRect;

public class OCRResponseMessage<T> {
    private Long sourceUploadID;

    public Long getSourceUploadID() {
        return sourceUploadID;
    }

    public void setSourceUploadID(Long sourceUploadID) {
        this.sourceUploadID = sourceUploadID;
    }

    private SourceUploadRect rect;

    public SourceUploadRect getRect() {
        return rect;
    }

    public void setRect(SourceUploadRect rect) {
        this.rect = rect;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
