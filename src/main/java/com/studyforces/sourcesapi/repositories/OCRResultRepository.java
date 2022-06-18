package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.OCRResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize("hasRole('editor')")
@RepositoryRestResource(path = "ocrResults")
public interface OCRResultRepository extends PagingAndSortingRepository<OCRResult, Long> {
}
