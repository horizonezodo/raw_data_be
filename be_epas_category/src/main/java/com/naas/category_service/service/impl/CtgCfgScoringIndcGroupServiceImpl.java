package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto;
import com.naas.category_service.dto.CtgInfProvince.CtgInfProvinceDto;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.model.CtgCfgScoringIndcGroup;
import com.naas.category_service.model.CtgCfgScoringIndcMapp;
import com.naas.category_service.model.CtgCfgScoringType;
import com.naas.category_service.repository.CtgCfgScoringIndcGroupRepository;
import com.naas.category_service.repository.CtgCfgScoringIndcMappRepository;
import com.naas.category_service.service.CtgCfgScoringIndcGroupService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgScoringIndcGroupServiceImpl implements CtgCfgScoringIndcGroupService {

    private final CtgCfgScoringIndcGroupRepository ctgCfgScoringIndcGroupRepository;
    private final ExcelService excelService;
    private final CtgCfgScoringIndcMappRepository ctgCfgScoringIndcMappRepository;

    @Override
    public Page<CtgCfgScoringIndcGroupDto> searchAll(String keyword, Pageable pageable){
        return ctgCfgScoringIndcGroupRepository.searchAll(keyword,pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels){
        List<CtgCfgScoringIndcGroupDto> ctgCfgScoringIndcGroupDtos=ctgCfgScoringIndcGroupRepository.exportToExcel(keyword);
        return excelService.exportToExcel(ctgCfgScoringIndcGroupDtos,labels,CtgCfgScoringIndcGroupDto.class,fileName);
    }

    @Override
    public void createScoringIndcGroup(CtgCfgScoringIndcGroupDto scoringIndcGroupDto){
        Optional<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroup=ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(scoringIndcGroupDto.getScoringIndcGroupCode());
        if(ctgCfgScoringIndcGroup.isPresent()){
            throw new BusinessException(CategoryErrorCode.SCORING_INDC_GROUP_ALREADY_EXISTS);
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
    public void updateScoringIndcGroup(CtgCfgScoringIndcGroupDto scoringIndcGroupDto){
        Optional<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroup=ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(scoringIndcGroupDto.getScoringIndcGroupCode());
        if(!ctgCfgScoringIndcGroup.isPresent()){
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
        if(!ctgCfgScoringIndcGroup.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        List<CtgCfgScoringIndcMapp> ctgCfgScoringIndcMapps = ctgCfgScoringIndcMappRepository.getCtgCfgScoringIndcMappsByScoringIndcGroupCode(scoringIndcGroupCode);
        if (!ctgCfgScoringIndcMapps.isEmpty()){
            throw new BusinessException(CategoryErrorCode.SCORING_INDC_GROUP_IS_USE);
        }


        ctgCfgScoringIndcGroupRepository.delete(ctgCfgScoringIndcGroup.get());
    }

    @Override
    public CtgCfgScoringIndcGroupDto getDetailScoringIndcGroup(String scoringIndcGroupCode){
        return ctgCfgScoringIndcGroupRepository.getDetail(scoringIndcGroupCode);
    }

    @Override
    public  boolean checkExist(String code){
        Optional<CtgCfgScoringIndcGroup> position = ctgCfgScoringIndcGroupRepository.findCtgCfgScoringIndcGroupByScoringIndcGroupCode(code);
        if(position.isPresent()) return true;
        return false;
    }
}
