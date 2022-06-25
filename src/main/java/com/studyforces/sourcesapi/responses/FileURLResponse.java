package com.studyforces.sourcesapi.responses;

import lombok.Builder;

@Builder
public class FileURLResponse {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
