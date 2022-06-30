package com.studyforces.sourcesapi.requests;

import com.studyforces.sourcesapi.models.OCRResult;
import lombok.Data;

import java.util.List;

@Data
public class UpdateSourceUploadOCRRequest {
    private List<OCRResult> ocrResults;
}
