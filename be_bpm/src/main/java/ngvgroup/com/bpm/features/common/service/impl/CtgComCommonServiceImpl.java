package ngvgroup.com.bpm.features.common.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.common.dto.CtgComCommonDTO;
import ngvgroup.com.bpm.features.common.mapper.CtgComCommonMapper;
import ngvgroup.com.bpm.features.common.repository.CtgComCommonRepository;
import ngvgroup.com.bpm.features.common.service.CtgComCommonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgComCommonServiceImpl implements CtgComCommonService {
    private final CtgComCommonMapper mapper;
    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode) {
        return ctgComCommonRepository.findAllByCommonTypeCode(commonTypeCode).stream()
                .map(mapper::toDto).toList();
    }
}
