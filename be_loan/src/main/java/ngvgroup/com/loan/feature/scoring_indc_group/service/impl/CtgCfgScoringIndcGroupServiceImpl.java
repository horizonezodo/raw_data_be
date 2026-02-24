package ngvgroup.com.loan.feature.scoring_indc_group.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2;
import ngvgroup.com.loan.feature.scoring_indc_group.model.CtgCfgScoringIndcGroup;
import ngvgroup.com.loan.feature.scoring_indc_group.repository.CtgCfgScoringIndcGroupRepository;
import ngvgroup.com.loan.feature.scoring_indc_group.service.CtgCfgScoringIndcGroupService;
import ngvgroup.com.loan.feature.scoring_indc_mapp.model.CtgCfgScoringIndcMapp;
import ngvgroup.com.loan.feature.scoring_indc_mapp.repository.CtgCfgScoringIndcMappRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CtgCfgScoringIndcGroupServiceImpl implements CtgCfgScoringIndcGroupService {
    private final CtgCfgScoringIndcGroupRepository ctgCfgScoringIndcGroupRepository;
    private final CtgCfgScoringIndcMappRepository ctgCfgScoringIndcMappRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<CtgCfgScoringIndcGroupDtoV2> getAllByTypeCode(String scoringTypeCode, String keyword, Pageable pageable) {
        return ctgCfgScoringIndcGroupRepository.getAllByTypeCode(scoringTypeCode,keyword, pageable);
    }

    @Override
    public Page<CtgCfgScoringIndcGroupDtoV2> searchAll(String keyword, Pageable pageable){
        return ctgCfgScoringIndcGroupRepository.searchAll(keyword,pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels){
        try {
            List<CtgCfgScoringIndcGroupDtoV2> ctgCfgScoringIndcGroupDtos=ctgCfgScoringIndcGroupRepository.exportToExcel(keyword);
            return exportExcel.exportExcel(ctgCfgScoringIndcGroupDtos, fileName);
        }
        catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public void createScoringIndcGroup(CtgCfgScoringIndcGroupDtoV2 scoringIndcGroupDto){
        Optional<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroup=ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(scoringIndcGroupDto.getScoringIndcGroupCode());
        if(ctgCfgScoringIndcGroup.isPresent()){
            throw new BusinessException(LoanErrorCode.SCORING_INDC_GROUP_ALREADY_EXISTS);
        }
        ctgCfgScoringIndcGroupRepository.save(new CtgCfgScoringIndcGroup(
                scoringIndcGroupDto.getScoringTypeCode(),
                scoringIndcGroupDto.getScoringIndcGroupCode(),
                scoringIndcGroupDto.getScoringIndcGroupName(),
                scoringIndcGroupDto.getScoringIndcGroupType(),
                scoringIndcGroupDto.getWeightScore(),
                scoringIndcGroupDto.getSortNumber(),
                scoringIndcGroupDto.getDescription()
        ));
    }

    @Override
    @Transactional
    public void updateScoringIndcGroup(CtgCfgScoringIndcGroupDtoV2 scoringIndcGroupDto){
        Optional<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroup=ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(scoringIndcGroupDto.getScoringIndcGroupCode());
        if(ctgCfgScoringIndcGroup.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgScoringIndcGroup.get().setScoringIndcGroupName(scoringIndcGroupDto.getScoringIndcGroupName());
        ctgCfgScoringIndcGroup.get().setWeightScore(scoringIndcGroupDto.getWeightScore());
        ctgCfgScoringIndcGroup.get().setSortNumber(scoringIndcGroupDto.getSortNumber());
        ctgCfgScoringIndcGroup.get().setDescription(scoringIndcGroupDto.getDescription());
        ctgCfgScoringIndcGroup.get().setScoringIndcGroupType(scoringIndcGroupDto.getScoringIndcGroupType());
        ctgCfgScoringIndcGroup.get().setScoringTypeCode(scoringIndcGroupDto.getScoringTypeCode());
        ctgCfgScoringIndcGroupRepository.save(ctgCfgScoringIndcGroup.get());
    }


    @Override
    public void deleteScoringIndcGroup(String scoringIndcGroupCode){
        Optional<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroup=ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(scoringIndcGroupCode);
        if(ctgCfgScoringIndcGroup.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        List<CtgCfgScoringIndcMapp> ctgCfgScoringIndcMapps = ctgCfgScoringIndcMappRepository.getCtgCfgScoringIndcMappsByScoringIndcGroupCode(scoringIndcGroupCode);
        if (!ctgCfgScoringIndcMapps.isEmpty()){
            throw new BusinessException(LoanErrorCode.SCORING_INDC_GROUP_IS_USE);
        }


        ctgCfgScoringIndcGroupRepository.delete(ctgCfgScoringIndcGroup.get());
    }

    @Override
    public CtgCfgScoringIndcGroupDtoV2 getDetailScoringIndcGroup(String scoringIndcGroupCode){
        return ctgCfgScoringIndcGroupRepository.getDetail(scoringIndcGroupCode);
    }

    @Override
    public boolean checkExist(String code) {
        return ctgCfgScoringIndcGroupRepository
                .findCtgCfgScoringIndcGroupByScoringIndcGroupCode(code)
                .isPresent();
    }
}
