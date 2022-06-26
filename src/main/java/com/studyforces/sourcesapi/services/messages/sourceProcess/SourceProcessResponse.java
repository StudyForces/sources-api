package com.studyforces.sourcesapi.services.messages.sourceProcess;

import lombok.Data;

@Data
public class SourceProcessResponse<T> {

    private Long sourceUploadID;

    private T data;

}
