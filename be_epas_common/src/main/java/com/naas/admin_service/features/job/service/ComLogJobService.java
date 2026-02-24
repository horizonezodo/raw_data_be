package com.naas.admin_service.features.job.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.naas.admin_service.features.job.dto.ComLogJobDto;

public interface ComLogJobService {
    Page<ComLogJobDto> search(String search, Pageable pageable);
}
