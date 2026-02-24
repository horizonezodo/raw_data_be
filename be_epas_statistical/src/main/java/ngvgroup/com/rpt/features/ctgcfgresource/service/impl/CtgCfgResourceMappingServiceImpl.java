package ngvgroup.com.rpt.features.ctgcfgresource.service.impl;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.ctgcfgresource.mapper.CtgCfgResourceMappingMapper;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.CtgCfgResourceMappingDTO;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.ResourceMappingDto;
import ngvgroup.com.rpt.features.ctgcfgresource.repository.ComCfgResourceMappingRepository;
import ngvgroup.com.rpt.features.ctgcfgresource.repository.CtgCfgResourceMappingRepository;
import ngvgroup.com.rpt.features.ctgcfgresource.service.CtgCfgResourceMappingService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CtgCfgResourceMappingServiceImpl extends BaseStoredProcedureService implements CtgCfgResourceMappingService {
    private final CtgCfgResourceMappingRepository repo;
    private final CtgCfgResourceMappingMapper ctgCfgResourceMappingMapper;
    private final ComCfgResourceMappingRepository comCfgResourceMappingRepository;

    @Override
    public List<CtgCfgResourceMappingDTO> getAllData(String resourceTypeCode) {
        return repo.getAllData(resourceTypeCode,getCurrentUserId());
    }

    @Override
    public ResponseData<List<ResourceMappingDto>> getListCurrentBranch() {
        String userid = getCurrentUserId();
        if (userid == null || userid.isEmpty()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Không thể lấy thông tin user hiện tại");
        }
        try {
            log.info("Lấy resource mapping từ DB với userId: {}", userid);
            List<ResourceMappingDto> dtos = ctgCfgResourceMappingMapper.toDtoList(
                    comCfgResourceMappingRepository.findAll().stream()
                            .filter(e -> userid.equals(e.getUserId()) && "CM032.001".equals(e.getResourceTypeCode()))
                            .toList());
            log.info("Kết quả resource mapping: {}", dtos);
            return new ResponseData<>(200, "OK", dtos);
        } catch (Exception ex) {
            log.error("Lỗi khi lấy resource mapping với userId: {}, error: {}", userid, ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi lấy danh sách branch");
        }
    }

    @Override
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("sub");
        }

        return null;
    }

}
