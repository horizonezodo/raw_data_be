package com.naas.admin_service.features.job.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.naas.admin_service.features.job.dto.ComLogJobDto;
import com.naas.admin_service.features.job.repository.ComLogJobRepository;
import com.naas.admin_service.features.job.service.ComLogJobService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComLogJobServiceImpl implements ComLogJobService {

    private final ComLogJobRepository logJobRepository;

    @Override
    public Page<ComLogJobDto> search(String search, Pageable pageable) {
        return logJobRepository.search(search, pageable);
    }

}
