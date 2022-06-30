package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.models.OCRResult;
import com.studyforces.sourcesapi.models.Problem;
import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.OCRResultRepository;
import com.studyforces.sourcesapi.requests.OCRResultUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/ocrResults")
@PreAuthorize("hasRole('editor')")
public class OCRResultsController {

    private final OCRResultRepository ocrResultRepository;

    OCRResultsController(OCRResultRepository ocrResultRepository) {
        this.ocrResultRepository = ocrResultRepository;
    }

    @GetMapping
    public Page<OCRResult> findAll(@NotNull final Pageable pageable) {
        return ocrResultRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OCRResult findById(@PathVariable Long id) {
        return ocrResultRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/{id}/upload")
    public SourceUpload findRelatedUploadById(@PathVariable Long id) {
        return ocrResultRepository.findById(id).orElseThrow(ResourceNotFoundException::new)
                .getSourceUpload();
    }

    @GetMapping("/{id}/problem")
    public Problem findRelatedProblemById(@PathVariable Long id) {
        return ocrResultRepository.findById(id).orElseThrow(ResourceNotFoundException::new)
                .getProblem();
    }

    @PostMapping
    public OCRResult create(@RequestBody OCRResultUpdateRequest upd) {
        return save(new OCRResult(), upd);
    }

    @PutMapping("/{id}")
    public OCRResult update(@PathVariable Long id, @RequestBody OCRResultUpdateRequest upd) {
        OCRResult ocrResult = ocrResultRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return save(ocrResult, upd);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ocrResultRepository.deleteById(id);
    }

    private OCRResult save(OCRResult ocrResult, OCRResultUpdateRequest upd) {
        ocrResult.setRect(upd.getRect());
        ocrResult.setData(upd.getData());

        return ocrResultRepository.save(ocrResult);
    }
}
