package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.models.OCRResult;
import com.studyforces.sourcesapi.models.Problem;
import com.studyforces.sourcesapi.repositories.OCRResultRepository;
import com.studyforces.sourcesapi.repositories.ProblemRepository;
import com.studyforces.sourcesapi.requests.ProblemUpdateRequest;
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
@RequestMapping("/problems")
@PreAuthorize("hasRole('editor')")
public class ProblemsController {

    private final ProblemRepository problemRepository;
    private final OCRResultRepository ocrResultRepository;

    ProblemsController(ProblemRepository problemRepository,
                       OCRResultRepository ocrResultRepository) {
        this.problemRepository = problemRepository;
        this.ocrResultRepository = ocrResultRepository;
    }

    @GetMapping
    public Page<Problem> findAll(@NotNull final Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Problem findById(@PathVariable Long id) {
        return problemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/{id}/ocrResults")
    public List<OCRResult> findOCRResultsById(@PathVariable Long id) {
        return problemRepository.findById(id).orElseThrow(ResourceNotFoundException::new)
                .getOcrResults();
    }

    @PostMapping
    public Problem create(@RequestBody ProblemUpdateRequest upd) {
        return save(new Problem(), upd);
    }

    @PutMapping("/{id}")
    public Problem update(@PathVariable Long id, @RequestBody ProblemUpdateRequest upd) {
        Problem problem = problemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return save(problem, upd);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        List<OCRResult> prevRes = problem.getOcrResults();
        if (prevRes != null) {
            ocrResultRepository.saveAll(prevRes.stream().peek(r -> r.setProblem(null)).toList());
        }
        problemRepository.delete(problem);
    }

    private Problem save(Problem problem, ProblemUpdateRequest upd) {
        problem.setType(upd.getType());
        problem.setProblem(upd.getProblem());
        problem.setSolution(upd.getSolution());
        problem.setSolverMetadata(upd.getSolverMetadata());
        problem.setAttachments(upd.getAttachments());

        List<OCRResult> prevRes = problem.getOcrResults();
        if (prevRes != null) {
            ocrResultRepository.saveAll(prevRes.stream().peek(r -> r.setProblem(null)).toList());
        }

        List<OCRResult> ocrResults = new ArrayList<>();
        Iterable<OCRResult> ocrResultsI = ocrResultRepository.findAllById(upd.getOcrResults());
        ocrResultsI.forEach(ocrResults::add);
        problem = problemRepository.save(problem);
        Problem finalProblem = problem;
        ocrResultRepository.saveAll(ocrResults.stream().peek(r -> r.setProblem(finalProblem)).toList());

        return problem;
    }
}
