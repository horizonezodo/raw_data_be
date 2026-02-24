package ngvgroup.com.hrm.feature.common.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.feature.common.dto.CommonDto;
import ngvgroup.com.hrm.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.hrm.feature.common.repository.CtgComCommonRepository;
import ngvgroup.com.hrm.feature.common.service.CtgComCommonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CtgComCommonServiceImpl implements CtgComCommonService {

    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode) {
        return ctgComCommonRepository.listCommon(commonTypeCode);
    }

    @Override
    public Map<String, String> findByCommonTypeCodes(List<String> commonTypeCodes) {
        return ctgComCommonRepository.findByCommonTypeCodeIn(commonTypeCodes)
                .stream()
                .collect(Collectors.toMap(CommonDto::getCommonCode, CommonDto::getCommonName));
    }
}
