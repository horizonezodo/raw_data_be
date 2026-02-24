package ngvgroup.com.hrm.feature.hrm_cfg_status.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.feature.hrm_cfg_status.dto.HrmCfgStatusOptionDto;
import ngvgroup.com.hrm.feature.hrm_cfg_status.repository.HrmCfgStatusRepository;
import ngvgroup.com.hrm.feature.hrm_cfg_status.service.HrmCfgStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrmCfgStatusServiceImpl implements HrmCfgStatusService {
    private final HrmCfgStatusRepository hrmCfgStatusRepository;

    @Override
    public List<HrmCfgStatusOptionDto> listOptions() {
        return hrmCfgStatusRepository.findAllByIsDelete(0).stream()
                .map(status -> new HrmCfgStatusOptionDto(
                        status.getOrgCode(),
                        status.getStatusCode(),
                        status.getStatusName()
                ))
                .toList();
    }
}
