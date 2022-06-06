package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.services.OCRService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ocr")
@PreAuthorize("hasRole('editor')")
public class OCRController {

    private final OCRService ocrService;
    private final SourceUploadRepository sourceUploadRepository;

    OCRController(OCRService ocrService, SourceUploadRepository sourceUploadRepository) {
        this.ocrService = ocrService;
        this.sourceUploadRepository = sourceUploadRepository;
    }

    @PostMapping("/request/{id}")
    SourceUpload requestOCR(@PathVariable Long id) throws Exception {
        SourceUpload upload = sourceUploadRepository.findById(id).orElseThrow();
        return this.ocrService.runOCR(upload);
    }

}
