package ngvgroup.com.rpt.features.ctgcfgresource.service;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.CtgCfgResourceMappingDTO;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.ResourceMappingDto;

import java.util.List;

public interface CtgCfgResourceMappingService {
    List<CtgCfgResourceMappingDTO> getAllData(String resourceTypeCode);
    ResponseData<List<ResourceMappingDto>> getListCurrentBranch();
}
