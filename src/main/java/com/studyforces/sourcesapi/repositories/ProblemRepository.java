package com.studyforces.sourcesapi.repositories;

import com.studyforces.sourcesapi.models.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Long> {
    @Query(value = "select p.* from problem p where cast(p.id as char) like :problemId%",
            countQuery = "select count(p.id) from problem p where cast(p.id as char) like :problemId%",
            nativeQuery = true)
    public Page<Problem> searchById(@Param("problemId") String problemId, Pageable pageable);
}
