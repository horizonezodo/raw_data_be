package ngvgroup.com.bpm.features.ctg_cfg_resource.service;

import ngvgroup.com.bpm.features.ctg_cfg_resource.dto.ResourceMappingDto;

import java.util.List;

public interface CtgCfgResourceMappingService {
    List<ResourceMappingDto> getListCurrentBranch();
}
