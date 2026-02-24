package com.naas.admin_service.features.job.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.naas.admin_service.features.job.dto.ComLogJobDto;
import com.naas.admin_service.features.job.model.ComLogJob;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComLogJobRepository extends JpaRepository<ComLogJob, Long> {

    @Query("""
        SELECT new com.naas.admin_service.features.job.dto.ComLogJobDto(
            j.id,
            j.jobId,
            j.jobName,
            j.status,
            j.startedAt,
            j.finishedAt,
            j.resultMessage
        ) FROM ComLogJob j
        WHERE (:search IS NULL
        OR LOWER(j.jobName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(j.status) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(j.resultMessage) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        """)
    Page<ComLogJobDto> search(String search, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ComLogJob l WHERE l.jobId = :jobId")
    int deleteByJobId(@Param("jobId") Long jobId);
}
