package com.studyforces.sourcesapi.services.messages.sourceProcess;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourceMetadataRequest {
    private Long sourceUploadID;
    private List<FileInfo> fileInfos;
}
