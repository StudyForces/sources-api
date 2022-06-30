package com.studyforces.sourcesapi.requests;

import com.studyforces.sourcesapi.models.OCRRect;
import lombok.Data;

@Data
public class OCRResultUpdateRequest {
    private OCRRect rect;
    private Object data;
}
