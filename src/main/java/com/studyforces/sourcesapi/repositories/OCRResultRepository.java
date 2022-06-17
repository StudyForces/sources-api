package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.OCRResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize("hasRole('editor')")
public interface OCRResultRepository extends PagingAndSortingRepository<OCRResult, Long> {
}
