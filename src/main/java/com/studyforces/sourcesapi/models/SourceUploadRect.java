package com.studyforces.sourcesapi.models;

import com.studyforces.sourcesapi.support.JpaConverterJson;

import javax.persistence.*;
import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    private SourceUploadRectType type;

    public SourceUploadRectType getType() {
        return type;
    }

    public void setType(SourceUploadRectType type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    private SourceUploadRectStatus status;

    public SourceUploadRectStatus getStatus() {
        return status;
    }

    public void setStatus(SourceUploadRectStatus status) {
        this.status = status;
    }

    private Object data;

    @Lob
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceUploadRect that = (SourceUploadRect) o;
        return getX().equals(that.getX()) && getY().equals(that.getY()) && getWidth().equals(that.getWidth()) && getHeight().equals(that.getHeight()) && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getWidth(), getHeight(), getType());
    }
}
