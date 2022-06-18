package com.studyforces.sourcesapi.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileInfoResponse {
    String contentType;
    Long size;
    Long lastModified;
}
