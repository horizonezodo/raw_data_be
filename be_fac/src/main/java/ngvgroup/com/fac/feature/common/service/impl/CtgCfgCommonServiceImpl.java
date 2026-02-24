package ngvgroup.com.fac.feature.common.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.common.repository.CtgCfgCommonRepository;
import ngvgroup.com.fac.feature.common.service.CtgCfgCommonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgCfgCommonServiceImpl implements CtgCfgCommonService {
    private final CtgCfgCommonRepository repo;

    @Override
    public List<CtgCfgCommonDTO> getByTypeCodeAndParentCode(String commonTypeCode, String parentCode) {
        return repo.getByTypeCodeAndParentCode(commonTypeCode, parentCode);
    }

    @Override
    public CtgCfgCommonDTO getCommonNameBuCommonValue(String commonValue) {
        CtgCfgCommonDTO common = repo.getCommonByCommonValue(commonValue);

        if (common == null) {
            throw new BusinessException(FacErrorCode.DATA_NOT_FOUND, commonValue
            );
        }
        return common;
    }

    @Override
    public List<CtgCfgCommonDTO> getList(List<String> commonCode) {
        return repo.getList(commonCode);
    }
}
