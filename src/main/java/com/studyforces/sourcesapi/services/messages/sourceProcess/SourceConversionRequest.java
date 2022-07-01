package com.studyforces.sourcesapi.services.messages.sourceProcess;

import com.studyforces.sourcesapi.models.SourceMetadata;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourceConversionRequest {
    private Long sourceUploadID;
    private SourceMetadata metadata;
    private List<FileInfo> fileInfos;
    private List<FileUploadResponse> uploadURLs;
}
