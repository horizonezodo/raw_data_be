package ngvgroup.com.bpmn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ngvgroup.com.bpmn.mapper.CtgCfgResourceMapping.CtgCfgResourceMappingMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.CtgCfgResourceMapping.ResourceMappingDto;
import ngvgroup.com.bpmn.service.CtgCfgResourceMappingService;

import com.ngvgroup.bpm.core.dto.ResponseData;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ngvgroup.com.bpmn.repository.CtgCfgResourceMappingRepository;

@Service
@RequiredArgsConstructor
public class CtgCfgResourceMappingServiceImpl implements CtgCfgResourceMappingService {
    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;
    private final CtgCfgResourceMappingMapper ctgCfgResourceMappingMapper;
    private static final Logger log = LoggerFactory.getLogger(CtgCfgResourceMappingServiceImpl.class);

    @Override
    public ResponseData<List<ResourceMappingDto>> getListCurrentBranch() {
        String userid = getCurrentUserId();
        if (userid == null || userid.isEmpty()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Không thể lấy thông tin user hiện tại");
        }
        try {
            log.info("Lấy resource mapping từ DB với userId: {}", userid);
            List<ResourceMappingDto> dtos = ctgCfgResourceMappingMapper.toDtoList(
                    ctgCfgResourceMappingRepository.findAll().stream()
                            .filter(e -> userid.equals(e.getUserId()) && "CM032.001".equals(e.getResourceTypeCode()))
                            .toList());
            log.info("Kết quả resource mapping: {}", dtos);
            return new ResponseData<>(200, "OK", dtos);
        } catch (Exception ex) {
            log.error("Lỗi khi lấy resource mapping với userId: {}, error: {}", userid, ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi lấy danh sách branch");
        }
    }

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("sub");
        }

        return null;
    }

    @Override
    public void deleteByResourceCode(String resourceCode) {
        ctgCfgResourceMappingRepository.deleteByResourceCode(resourceCode);
    }
}
