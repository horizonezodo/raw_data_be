package ngvgroup.com.bpmn.service;

import java.util.List;

import com.ngvgroup.bpm.core.dto.ResponseData;

import ngvgroup.com.bpmn.dto.CtgCfgResourceMapping.ResourceMappingDto;

public interface CtgCfgResourceMappingService {
    ResponseData<List<ResourceMappingDto>> getListCurrentBranch();

    void deleteByResourceCode(String resourceCode);
}
