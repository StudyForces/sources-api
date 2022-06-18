package com.studyforces.sourcesapi.services.messages;

import com.studyforces.sourcesapi.models.OCRRect;

public class OCRRequestMessage {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Long ocrResultID;

    public Long getOcrResultID() {
        return this.ocrResultID;
    }

    public void setOcrResultID(Long ocrResultID) {
        this.ocrResultID = ocrResultID;
    }

    private OCRRect rect;

    public OCRRect getRect() {
        return rect;
    }

    public void setRect(OCRRect rect) {
        this.rect = rect;
    }
}
