package com.studyforces.sourcesapi.services.messages.sourceProcess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceMetadataRequest {
    private String url;
    private Long sourceUploadID;
}
