package com.studyforces.sourcesapi.services.messages.ocr;

public class OCRResponseMessage<T> {
    private Long ocrResultID;

    public Long getOcrResultID() {
        return this.ocrResultID;
    }

    public void setOcrResultID(Long ocrResultID) {
        this.ocrResultID = ocrResultID;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
