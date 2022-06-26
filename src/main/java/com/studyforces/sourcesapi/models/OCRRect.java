package com.studyforces.sourcesapi.models;

import javax.persistence.*;

@Embeddable
public class OCRRect {

    private Long page;

    public Long getPage() {
        return this.page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

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

}
