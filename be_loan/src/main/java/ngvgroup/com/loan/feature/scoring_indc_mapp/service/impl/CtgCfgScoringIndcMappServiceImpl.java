package ngvgroup.com.loan.feature.scoring_indc_mapp.service.impl;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import ngvgroup.com.loan.feature.scoring_indc_mapp.model.CtgCfgScoringIndcMapp;
import ngvgroup.com.loan.feature.scoring_indc_mapp.repository.CtgCfgScoringIndcMappRepository;
import ngvgroup.com.loan.feature.scoring_indc_mapp.service.CtgCfgScoringIndcMappService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtgCfgScoringIndcMappServiceImpl extends BaseStoredProcedureService implements CtgCfgScoringIndcMappService {
    private final CtgCfgScoringIndcMappRepository ctgCfgScoringIndcMappRepository;

    @Override
    public void createIndcMapp(Map<String, List<CtgCfgScoringIndcDto>> dtos) {
        for(Map.Entry<String, List<CtgCfgScoringIndcDto>> entry : dtos.entrySet()){
            String groupCode = entry.getKey();
            List<CtgCfgScoringIndcDto> incomingDtos = entry.getValue();
            List<CtgCfgScoringIndcMapp> existingMapps = ctgCfgScoringIndcMappRepository.findAllByScoringIndcGroupCode(groupCode);
            Map<String, CtgCfgScoringIndcDto> incomingDtoMap = incomingDtos.stream()
                    .collect(Collectors.toMap(CtgCfgScoringIndcDto::getIndicatorCode, dto -> dto));
            for (CtgCfgScoringIndcMapp mapp : existingMapps) {
                String code = mapp.getIndicatorCode();
                if (incomingDtoMap.containsKey(code)) {
                    CtgCfgScoringIndcDto dto = incomingDtoMap.get(code);
                    mapp.setSortNumber(dto.getSortNumber());
                    ctgCfgScoringIndcMappRepository.save(mapp);
                    incomingDtoMap.remove(code);
                } else {
                    ctgCfgScoringIndcMappRepository.delete(mapp);
                }
            }
            for (CtgCfgScoringIndcDto dto : incomingDtoMap.values()) {
                CtgCfgScoringIndcMapp newMapp = new CtgCfgScoringIndcMapp();
                newMapp.setScoringIndcGroupCode(groupCode);
                newMapp.setIndicatorCode(dto.getIndicatorCode());
                newMapp.setIsActive(1);
                newMapp.setIsDelete(0);
                newMapp.setRecordStatus(LoanVariableConstants.APPROVAL);
                newMapp.setApprovedBy(getCurrentUserName());
                newMapp.setCreatedBy(getCurrentUserName());
                newMapp.setSortNumber(dto.getSortNumber());

                ctgCfgScoringIndcMappRepository.save(newMapp);
            }
        }
    }

    @Override
    @Modifying
    @Transactional
    public void deleteByGroupCode(String groupCode) {
        this.ctgCfgScoringIndcMappRepository.deleteAllByScoringIndcGroupCode(groupCode);
    }
}
