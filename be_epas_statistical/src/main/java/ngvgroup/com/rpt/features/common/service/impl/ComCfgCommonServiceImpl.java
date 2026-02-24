package ngvgroup.com.rpt.features.common.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.common.repository.ComCfgCommonRepository;
import ngvgroup.com.rpt.features.common.service.ComCfgCommonService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ComCfgCommonServiceImpl implements ComCfgCommonService {
    private final ComCfgCommonRepository comCfgCommonRepository;
    @Override
    public List<ComCfgCommonDto> listCommonByCommonTypeCode(String commonTypeCode) {
        return this.comCfgCommonRepository.getAllCommonTypeCode(commonTypeCode);
    }
    @Override
    public List<ComCfgCommonDto> getAllBy(){
        return comCfgCommonRepository.getAllBy();
    }

    @Override
    public List<CommonResponse> getAllCommonByCommonTypeCode(String commonTypeCode) {
        return comCfgCommonRepository.getAllCommonByCommonTypeCode(commonTypeCode);
    }

    @Override
    public List<ComCfgCommonDto> getAllCommonByParentCode(String parentCode) {
        return comCfgCommonRepository.findAllByParentCode(parentCode);
    }

    @Override
    public List<ComCfgCommonDto> getAllByListCommonTypeCode(List<String> commonTypeCodes) {
        return comCfgCommonRepository.findAllByListCommonTypeCode(commonTypeCodes);
    }

    @Override
    public List<CommonDto> getAllCommon() {
        return comCfgCommonRepository.getAllCommon();
    }
    @Override
    public List<ComCfgCommonDto> getAllByParentCodeAndTypeCode(String parentCode, String commonTypeCode) {
        return comCfgCommonRepository.findAllByParentCodeAndCommonTypeCode(parentCode, commonTypeCode);
    }
}
