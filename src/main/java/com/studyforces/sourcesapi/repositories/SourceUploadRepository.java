package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.SourceUpload;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize("hasRole('editor')")
public interface SourceUploadRepository extends PagingAndSortingRepository<SourceUpload, Long> {
}
