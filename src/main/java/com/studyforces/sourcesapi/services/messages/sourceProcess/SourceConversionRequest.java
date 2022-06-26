package com.studyforces.sourcesapi.services.messages.sourceProcess;

import com.studyforces.sourcesapi.models.SourceMetadata;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourceConversionRequest {
    private String url;
    private Long sourceUploadID;
    private SourceMetadata metadata;
    private List<FileUploadResponse> uploadURLs;
}
