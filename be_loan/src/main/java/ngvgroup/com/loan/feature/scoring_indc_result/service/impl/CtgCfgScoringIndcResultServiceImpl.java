package ngvgroup.com.loan.feature.scoring_indc_result.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.scoring_indc_result.dto.CtgCfgScoringIndcResultDTO;
import ngvgroup.com.loan.feature.scoring_indc_result.model.CtgCfgScoringIndcResult;
import ngvgroup.com.loan.feature.scoring_indc_result.repository.CtgCfgScoringIndcResultRepository;
import ngvgroup.com.loan.feature.scoring_indc_result.service.CtgCfgScoringIndcResultService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtgCfgScoringIndcResultServiceImpl extends BaseStoredProcedureService implements CtgCfgScoringIndcResultService {
    private final CtgCfgScoringIndcResultRepository ctgCfgScoringIndcResultRepository;
    @Override
    public void save(List<CtgCfgScoringIndcResultDTO> dto, String indicatorCode) {
        for(CtgCfgScoringIndcResultDTO ctg:dto){
            validateResult(ctg);
            this.saveOne(ctg,indicatorCode);
        }
    }

    @Override
    public void update(List<CtgCfgScoringIndcResultDTO> dto, String indicatorCode) {
        List<CtgCfgScoringIndcResult> lst = ctgCfgScoringIndcResultRepository.findAllByIndicatorCodeOrderBySortNumberAsc(indicatorCode);
        Set<String> dtoKeySet = dto.stream()
                .map(d -> buildKey(indicatorCode, d))
                .collect(Collectors.toSet());
        for (CtgCfgScoringIndcResultDTO d : dto) {
            validateResult(d);
            String key = buildKey(indicatorCode, d);
            Optional<CtgCfgScoringIndcResult> existing = lst.stream()
                    .filter(db -> buildKey(indicatorCode, db).equals(key))
                    .findFirst();

            if (existing.isPresent()) {
                CtgCfgScoringIndcResult entity = existing.get();
                BeanUtils.copyProperties(d, entity);
                ctgCfgScoringIndcResultRepository.save(entity);
            } else {
                this.saveOne(d, indicatorCode);
            }
        }

        for (CtgCfgScoringIndcResult db : lst) {
            String key = buildKey(indicatorCode, db);
            if (!dtoKeySet.contains(key)) {
                ctgCfgScoringIndcResultRepository.delete(db);
            }
        }
    }

    private String buildKey(String indicatorCode, CtgCfgScoringIndcResultDTO dto) {
        return indicatorCode + "|" + dto.getResultName() + "|" + dto.getScoreValue() + "|" + dto.getScoreValueMax();
    }

    private String buildKey(String indicatorCode, CtgCfgScoringIndcResult entity) {
        return indicatorCode + "|" + entity.getResultName() + "|" + entity.getScoreValue() + "|" + entity.getScoreValueMax();
    }

    private void saveOne(CtgCfgScoringIndcResultDTO dto,String indicatorCode){
        CtgCfgScoringIndcResult ctgCfgScoringIndcResult = new CtgCfgScoringIndcResult();
        BeanUtils.copyProperties(dto,ctgCfgScoringIndcResult);
        ctgCfgScoringIndcResult.setIsActive(1);
        ctgCfgScoringIndcResult.setIsDelete(0);
        ctgCfgScoringIndcResult.setCreatedBy(getCurrentUserName());
        ctgCfgScoringIndcResult.setRecordStatus(LoanVariableConstants.APPROVAL);
        ctgCfgScoringIndcResult.setIndicatorCode(indicatorCode);
        ctgCfgScoringIndcResult.setApprovedBy(getCurrentUserName());
        ctgCfgScoringIndcResultRepository.save(ctgCfgScoringIndcResult);
    }

    @Override
    public void delete(String indicatorCode) {
        List<CtgCfgScoringIndcResult> ctgCfgScoringIndcResults = ctgCfgScoringIndcResultRepository.findAllByIndicatorCodeOrderBySortNumberAsc(indicatorCode);
        ctgCfgScoringIndcResultRepository.deleteAll(ctgCfgScoringIndcResults);
    }

    @Override
    public List<CtgCfgScoringIndcResultDTO> getAllResult(String indicatorCode) {
        List<CtgCfgScoringIndcResult> ctgCfgScoringIndcResults = ctgCfgScoringIndcResultRepository.findAllByIndicatorCodeOrderBySortNumberAsc(indicatorCode);
        List<CtgCfgScoringIndcResultDTO> dtos = new ArrayList<>();
        for(CtgCfgScoringIndcResult c:ctgCfgScoringIndcResults){
            CtgCfgScoringIndcResultDTO dto = new CtgCfgScoringIndcResultDTO();
            BeanUtils.copyProperties(c,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    void validateResult(CtgCfgScoringIndcResultDTO dto){
        if (dto.getScoreValue() != null && dto.getScoreValueMax() != null && dto.getScoreValueMin() != null) {
            if (dto.getScoreValue().compareTo(dto.getScoreValueMax()) > 0 ||
                    dto.getScoreValue().compareTo(dto.getScoreValueMin()) < 0) {
                throw new BusinessException(LoanErrorCode.SCORE_VALUE_EXCEPTION);
            }else if (dto.getScoreValueMax().compareTo(dto.getScoreValue()) < 0 || dto.getScoreValueMax().compareTo(dto.getScoreValueMin()) < 0){
                throw new BusinessException(LoanErrorCode.SCORE_VALUE_MAX_EXCEPTION);
            }else if(dto.getScoreValueMin().compareTo(dto.getScoreValue()) > 0 || dto.getScoreValueMin().compareTo(dto.getScoreValueMax()) > 0){
                throw new BusinessException(LoanErrorCode.SCORE_VALUE_MIN_EXCEPTION);
            }
        }

    }
}
