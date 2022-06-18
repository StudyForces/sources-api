package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.OCRResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
        collectionResourceRel = "ocrResults",
        itemResourceRel = "ocrResult",
        path = "ocrResults")
public interface OCRResultRepository extends PagingAndSortingRepository<OCRResult, Long> {
}
