package ngvgroup.com.loan.feature.type_of_capital_use.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseRateDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.mapper.LnmCfgCapitalUseRateMapper;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUseRate;
import ngvgroup.com.loan.feature.type_of_capital_use.repository.LnmCfgCapitalUseRateRepository;
import ngvgroup.com.loan.feature.type_of_capital_use.service.LnmCfgCapitalUseRateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LnmCfgCapitalUseRateServiceImpl implements LnmCfgCapitalUseRateService {
    private final LnmCfgCapitalUseRateRepository capitalUseRateRepository;
    private final LnmCfgCapitalUseRateMapper mapper;

    @Override
    @Transactional
    public void saveOrUpdateAll(List<LnmCfgCapitalUseRateDTO> dtos,String type) {
        this.validateRates(dtos);

        Set<String> dtoCodes = dtos.stream()
                .map(LnmCfgCapitalUseRateDTO::getCapitalUseCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (LoanVariableConstants.CREATE.equalsIgnoreCase(type)) {
            handleCreate(dtos, dtoCodes);
            return;
        }

        if (LoanVariableConstants.UPDATE.equalsIgnoreCase(type)) {
            handleUpdate(dtos, dtoCodes);
        }
    }

    private void validateRates(List<LnmCfgCapitalUseRateDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        // Group theo effectiveDate + rateValueType
        Map<String, List<LnmCfgCapitalUseRateDTO>> groupMap = new HashMap<>();

        for (LnmCfgCapitalUseRateDTO dto : dtos) {
            String key = dto.getEffectiveDate() + "_" + dto.getRateValueType();
            groupMap.computeIfAbsent(key, k -> new ArrayList<>()).add(dto);
        }

        BigDecimal expected = BigDecimal.valueOf(100);

        for (Map.Entry<String, List<LnmCfgCapitalUseRateDTO>> entry : groupMap.entrySet()) {
            List<LnmCfgCapitalUseRateDTO> group = entry.getValue();

            // ✅ 1. Check trùng useLevel trong cùng group
            Set<String> useLevels = new HashSet<>();
            for (LnmCfgCapitalUseRateDTO dto : group) {
                String useLevel = dto.getUseLevel();
                if (!useLevels.add(useLevel)) {
                    throw new BusinessException(
                            LoanErrorCode.CAPITAL_USE_LEVEL_DUPLICATE,
                            useLevel
                    );
                }
            }

            // ✅ 2. Check tổng rateValue = 100 trong cùng group
            BigDecimal sum = group.stream()
                    .map(dto -> dto.getRateValue() != null ? dto.getRateValue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sum.compareTo(expected) != 0) {
                throw new BusinessException(
                        LoanErrorCode.CAPITAL_USE_RATE_SUM_INVALID,
                        sum
                );
            }
        }
    }

    @Override
    @Transactional
    public void deleteAll(String capitalUseCode, String orgCode) {
        this.capitalUseRateRepository.deleteAllByCapitalUseCodeAndOrgCode(capitalUseCode,orgCode );
    }

    @Override
    public List<LnmCfgCapitalUseRateDTO> getAllByCapitalUseCodeAndOrgCode(String capitalUseCode, String orgCode) {
        return this.mapper.toListDto(this.capitalUseRateRepository.findAllByCapitalUseCodeAndOrgCode(capitalUseCode, orgCode));
    }

    private void handleCreate(List<LnmCfgCapitalUseRateDTO> dtos, Set<String> dtoCodes) {

            // check code đã tồn tại chưa
            List<String> existedCodes = capitalUseRateRepository.findExistingCodes(dtoCodes, dtos.get(0).getOrgCode());

            if (!existedCodes.isEmpty()) {
                throw new BusinessException(ErrorCode.CONFLICT, existedCodes);
            }

            // map DTO -> Entity
            List<LnmCfgCapitalUseRate> entities = mapper.toListEntity(dtos);

        capitalUseRateRepository.saveAll(entities);
    }

    private void handleUpdate(List<LnmCfgCapitalUseRateDTO> dtos, Set<String> dtoCodes) {
        this.capitalUseRateRepository.deleteAllByCapitalUseCodeAndOrgCode(dtos.get(0).getCapitalUseCode(), dtos.get(0).getOrgCode());
        this.handleCreate(dtos,dtoCodes);
    }

}
