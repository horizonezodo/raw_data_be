package ngvgroup.com.rpt.features.comcfg.service.impl;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;


import ngvgroup.com.rpt.features.comcfg.feign.ComCfgTemplateFeign;
import ngvgroup.com.rpt.features.comcfg.service.ComCfgTemplateService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.*;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor

public class ComCfgTemplateServiceImpl implements ComCfgTemplateService {

    private final ComCfgTemplateFeign comCfgTemplateFeign;

    @Override
    public ResponseEntity<Object> getAll(String keyword, Pageable pageable) {

        ResponseData<Object> response = comCfgTemplateFeign.getAll(
                keyword,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        return ResponseEntity.ok(response);
    }
}
