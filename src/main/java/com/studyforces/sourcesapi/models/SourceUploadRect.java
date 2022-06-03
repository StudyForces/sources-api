package com.studyforces.sourcesapi.models;

import javax.persistence.Embeddable;

@Embeddable
public class SourceUploadRect {

    private Double x;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    private Double y;

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    private Double width;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    private Double height;

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    private SourceUploadRectType type;

    public SourceUploadRectType getType() {
        return type;
    }

    public void setType(SourceUploadRectType type) {
        this.type = type;
    }

    private SourceUploadRectStatus status;

    public SourceUploadRectStatus getStatus() {
        return status;
    }

    public void setStatus(SourceUploadRectStatus status) {
        this.status = status;
    }
}
