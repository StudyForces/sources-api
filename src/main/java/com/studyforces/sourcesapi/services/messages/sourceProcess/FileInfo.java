package com.studyforces.sourcesapi.services.messages.sourceProcess;

import com.studyforces.sourcesapi.responses.FileInfoResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfo {
    private String url;
    private FileInfoResponse fileInfo;
}
