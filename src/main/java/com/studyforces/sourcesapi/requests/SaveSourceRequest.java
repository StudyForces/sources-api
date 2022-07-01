package com.studyforces.sourcesapi.requests;

import java.util.List;

public class SaveSourceRequest {
    private List<String> fileNames;

    public List<String> getFileNames() {
        return this.fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
