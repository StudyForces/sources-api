package com.studyforces.sourcesapi.models;

import javax.persistence.*;

@Entity
public class SourceUploadRect {
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
    private Double x;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Basic
    private Double y;

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Basic
    private Double width;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Basic
    private Double height;

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @ManyToOne
    private SourceUpload upload;

    public SourceUpload getUpload() {
        return upload;
    }

    public void setUpload(SourceUpload upload) {
        this.upload = upload;
    }

    @Enumerated(EnumType.STRING)
    private SourceUploadRectType type;

    public SourceUploadRectType getType() {
        return type;
    }

    public void setType(SourceUploadRectType type) {
        this.type = type;
    }

    @Enumerated(EnumType.ORDINAL)
    private SourceUploadRectStatus status;

    public SourceUploadRectStatus getStatus() {
        return status;
    }

    public void setStatus(SourceUploadRectStatus status) {
        this.status = status;
    }
}
