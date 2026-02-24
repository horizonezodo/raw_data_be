package ngvgroup.com.bpm.features.ctg_cfg_resource.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.features.ctg_cfg_resource.dto.ResourceMappingDto;
import ngvgroup.com.bpm.features.ctg_cfg_resource.mapper.CtgCfgResourceMappingMapper;
import ngvgroup.com.bpm.features.ctg_cfg_resource.model.CtgCfgResourceMapping;
import ngvgroup.com.bpm.features.ctg_cfg_resource.repository.CtgCfgResourceMappingRepository;
import ngvgroup.com.bpm.features.ctg_cfg_resource.service.CtgCfgResourceMappingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CtgCfgResourceMappingServiceImpl extends BaseServiceImpl<CtgCfgResourceMapping, ResourceMappingDto>implements CtgCfgResourceMappingService {
    private final CtgCfgResourceMappingRepository resourceMappingRepository;
    private final CtgCfgResourceMappingMapper resourceMapper;

    protected CtgCfgResourceMappingServiceImpl(CtgCfgResourceMappingRepository resourceMappingRepository, CtgCfgResourceMappingMapper resourceMapper) {
        super(resourceMappingRepository, resourceMapper);
        this.resourceMappingRepository = resourceMappingRepository;
        this.resourceMapper = resourceMapper;
    }


    @Override
    public List<ResourceMappingDto> getListCurrentBranch() {
        String userid = getCurrentUserId();
        if (userid == null || userid.isEmpty()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Không thể lấy thông tin user hiện tại");
        }

        return resourceMapper.toListDto(
                resourceMappingRepository.findAll().stream()
                        .filter(e -> userid.equals(e.getUserId()) && "CM032.001".equals(e.getResourceTypeCode()))
                        .toList());
    }
}
