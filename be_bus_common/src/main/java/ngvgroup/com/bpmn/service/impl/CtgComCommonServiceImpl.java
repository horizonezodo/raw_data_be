package ngvgroup.com.bpmn.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.bpmn.dto.ComCommon.CommonDto;
import ngvgroup.com.bpmn.dto.CtgComCommon.CtgComCommonDTO;
import ngvgroup.com.bpmn.mapper.CtgComCommon.CtgComCommonMapper;
import ngvgroup.com.bpmn.repository.CtgComCommonRepository;
import ngvgroup.com.bpmn.service.CtgComCommonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CtgComCommonServiceImpl implements CtgComCommonService {
    private final CtgComCommonMapper mapper;
    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CommonDto> listCommon(String commonTypeCode) {
        return ctgComCommonRepository.listCommon(commonTypeCode);
    }

    @Override
    public List<CommonDto> getAllCommon() {
        return ctgComCommonRepository.getAllCommon();
    }

     @Override
    public List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode) {
        return ctgComCommonRepository.findAllByCommonTypeCode(commonTypeCode).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }
}
