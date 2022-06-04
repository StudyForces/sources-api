package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.SourceUpload;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SourceUploadRepository extends PagingAndSortingRepository<SourceUpload, Long> {
}
