package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.OCRResult;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OCRResultRepository extends PagingAndSortingRepository<OCRResult, Long> {
}
