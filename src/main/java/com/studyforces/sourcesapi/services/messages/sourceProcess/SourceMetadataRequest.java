package com.studyforces.sourcesapi.services.messages.sourceProcess;

import com.studyforces.sourcesapi.responses.FileInfoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceMetadataRequest {
    private String url;
    private Long sourceUploadID;
    private FileInfoResponse fileInfo;
}
