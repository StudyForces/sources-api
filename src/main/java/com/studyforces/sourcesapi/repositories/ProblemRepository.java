package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.Problem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Long> {
}
