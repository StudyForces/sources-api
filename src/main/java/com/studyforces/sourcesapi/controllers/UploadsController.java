package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.models.OCRResult;
import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.OCRResultRepository;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.requests.SaveSourceRequest;
import com.studyforces.sourcesapi.requests.UpdateSourceUploadOCRRequest;
import com.studyforces.sourcesapi.responses.FileInfoResponse;
import com.studyforces.sourcesapi.services.FileService;
import com.studyforces.sourcesapi.services.SourceService;
import io.minio.StatObjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/uploads")
@PreAuthorize("hasRole('editor')")
public class UploadsController {
    private final FileService fileService;
    private final SourceService sourceService;
    private final SourceUploadRepository sourceUploadRepository;
    private final OCRResultRepository ocrResultRepository;

    public UploadsController(FileService fileService,
                             SourceUploadRepository sourceUploadRepository,
                             SourceService sourceService,
                             OCRResultRepository ocrResultRepository) {
        this.fileService = fileService;
        this.sourceUploadRepository = sourceUploadRepository;
        this.sourceService = sourceService;
        this.ocrResultRepository = ocrResultRepository;
    }

    @PostMapping
    SourceUpload saveSource(@RequestBody SaveSourceRequest req) throws Exception {
        if (!fileService.fileExists(req.getFileName())) {
            throw new ResourceNotFoundException();
        }

        SourceUpload upload = new SourceUpload();
        upload.setSourceFile(req.getFileName());

        upload = sourceUploadRepository.save(upload);

        sourceService.process(upload);

        return upload;
    }

    @GetMapping
    public Page<SourceUpload> findAll(@NotNull final Pageable pageable) {
        return sourceUploadRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public SourceUpload findById(@PathVariable Long id) {
        return sourceUploadRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sourceUploadRepository.deleteById(id);
    }

    @GetMapping("/{id}/ocrResults")
    public List<OCRResult> findOCRResultsById(@PathVariable Long id) {
        return sourceUploadRepository.findById(id).orElseThrow(ResourceNotFoundException::new)
                .getOcrResults();
    }

    @PostMapping("/{id}/ocrResults")
    public List<OCRResult> setOCRResultForId(@PathVariable Long id, @RequestBody UpdateSourceUploadOCRRequest req) {
        SourceUpload upload = sourceUploadRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        List<OCRResult> ocrResults = new ArrayList<>();
        ocrResultRepository.saveAll(req.getOcrResults().stream()
                .peek(r -> r.setSourceUpload(upload)).toList()).forEach(ocrResults::add);

        return ocrResults;
    }

    @PostMapping("/{id}/convert")
    public void convert(@PathVariable Long id) throws Exception {
        sourceService.process(sourceUploadRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    @GetMapping("/{id}/info")
    FileInfoResponse fileInfo(@PathVariable Long id) throws Exception {
        SourceUpload upload = sourceUploadRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        StatObjectResponse stat = fileService.fileInfo(upload.getSourceFile());

        return FileInfoResponse.builder()
                .contentType(stat.contentType())
                .size(stat.size())
                .lastModified(stat.lastModified().toEpochSecond())
                .build();
    }
}
