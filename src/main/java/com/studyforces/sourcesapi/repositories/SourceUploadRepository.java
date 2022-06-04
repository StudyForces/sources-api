package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.SourceUpload;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.PostMapping;

public interface SourceUploadRepository extends PagingAndSortingRepository<SourceUpload, Long> {
}
