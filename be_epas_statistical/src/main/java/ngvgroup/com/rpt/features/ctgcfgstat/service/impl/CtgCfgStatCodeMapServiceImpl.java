package ngvgroup.com.rpt.features.ctgcfgstat.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.CtgCfgStatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMappingResponse;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatCodeMap;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import ngvgroup.com.rpt.features.ctgcfgstat.repository.CtgCfgStatCodeMapRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatCodeMapService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtgCfgStatCodeMapServiceImpl implements CtgCfgStatCodeMapService {
    private final CtgCfgStatCodeMapRepository repo;
    private final JdbcTemplate jdbcTemplate;

    private final CtgCfgStatTypeRepository ctgCfgStatTypeRepos;

    @Override
    public List<StatCodeMappingResponse> queryInternalStatByTypeCode(String statTypeCode) {

        CtgCfgStatType type = validateAndGetStatType(statTypeCode);

        String expressionSql = type.getExpressionSql();
        if (isBlankSql(expressionSql)) {
            return emptyResult();
        }

        validateSqlStartsWithSelect(expressionSql);

        List<Map<String, Object>> resultSet = executeSqlSafely(expressionSql);
        if (resultSet.isEmpty()) {
            return emptyResult();
        }

        List<StatCodeMappingResponse> baseResults = mapToResponses(resultSet);
        if (baseResults.isEmpty()) {
            return emptyResult();
        }

        List<String> codeList = baseResults.stream()
                .map(StatCodeMappingResponse::getCode)
                .toList();

        List<Object[]> mappings = repo.getStatRegulatoryCodes(codeList, statTypeCode);

        return expandWithRegulatoryCodes(baseResults, mappings);
    }



    @Override
    public List<StatCodeMapDto> getDataMapping(String statTypeCode, List<String> internalCodes) {
        if (statTypeCode == null || statTypeCode.isBlank() ||
                internalCodes == null || internalCodes.isEmpty()) {
            return List.of();
        }
        List<CtgCfgStatCodeMap> mappings = repo.findByStatTypeCodeAndStatInternalCodeIn(statTypeCode, internalCodes);
        if (!mappings.isEmpty()) {
            return mappings.stream()
                    .map(m -> StatCodeMapDto.builder()
                            .statTypeCode(m.getStatTypeCode())
                            .statRegulatoryCode(m.getStatRegulatoryCode())
                            .statInternalCode(m.getStatInternalCode())
                            .build())
                    .toList();
        }
        return List.of();
    }

    @Override
    @Transactional
    public List<CtgCfgStatCodeMap> saveCodeMapping(CtgCfgStatCodeMapDto dto) {
        List<String> internalCodes = dto.getStatInternalCode();
        if (internalCodes == null || internalCodes.isEmpty()) {
            CtgCfgStatCodeMap records = CtgCfgStatCodeMap.builder()
                    .reportModuleCode(dto.getReportModuleCode())
                    .statTypeCode(dto.getStatTypeCode())
                    .statRegulatoryCode(dto.getStatRegulatoryCode())
                    .statInternalCode(null)
                    .orgCode("%")
                    .build();
            records.setRecordStatus(VariableConstants.DD);
            return List.of(repo.save(records));
        }
        return internalCodes.stream()
                .map(internalCode -> {
                    Optional<CtgCfgStatCodeMap> existingOpt = repo.findByStatTypeCodeAndStatInternalCode(dto.getStatTypeCode(), internalCode);
                    CtgCfgStatCodeMap entity;
                    if (existingOpt.isPresent()) {
                        entity = existingOpt.get();
                        entity.setStatRegulatoryCode(dto.getStatRegulatoryCode());
                    } else {
                        entity = CtgCfgStatCodeMap.builder()
                                .reportModuleCode(dto.getReportModuleCode())
                                .statTypeCode(dto.getStatTypeCode())
                                .statRegulatoryCode(dto.getStatRegulatoryCode())
                                .statInternalCode(internalCode)
                                .orgCode("%")
                                .build();
                        entity.setRecordStatus(VariableConstants.DD);
                    }
                    return repo.save(entity);
                }).toList();

    }

    @Override
    public void deleteStatCodeMapByStatTypeCode(String statTypeCode, String statRegulatoryCode) {
        List<CtgCfgStatCodeMap> existing = repo.findByStatTypeCodeAndStatRegulatoryCode(statTypeCode, statRegulatoryCode);
        if (existing.isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        repo.deleteAll(existing);
    }

    private CtgCfgStatType validateAndGetStatType(String statTypeCode) {
        CtgCfgStatType type = ctgCfgStatTypeRepos.findByStatTypeCode(statTypeCode);
        if (type == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        return type;
    }

    private boolean isBlankSql(String sql) {
        return sql == null || sql.isBlank();
    }

    private void validateSqlStartsWithSelect(String sql) {
        String trim = sql.trim().toLowerCase(Locale.ROOT);
        if (!trim.startsWith("select")) {
            throw new BusinessException(StatisticalErrorCode.BAD_REQUEST);
        }
    }

    private List<StatCodeMappingResponse> emptyResult() {
        return List.of(StatCodeMappingResponse.builder()
                .code(null)
                .name(null)
                .build());
    }

    private List<Map<String, Object>> executeSqlSafely(String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<StatCodeMappingResponse> mapToResponses(List<Map<String, Object>> resultSet) {
        return resultSet.stream()
                .filter(row -> row.containsKey("code") && row.containsKey("name"))
                .map(row -> StatCodeMappingResponse.builder()
                        .code(asString(row.get("code")))
                        .name(asString(row.get("name")))
                        .build())
                .filter(item -> item.getCode() != null && item.getName() != null)
                .toList();
    }

    private String asString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private List<StatCodeMappingResponse> expandWithRegulatoryCodes(
            List<StatCodeMappingResponse> baseItems,
            List<Object[]> mappings) {

        Map<String, String> mappingMap = mappings.stream()
                .filter(row -> row != null && row.length >= 2)
                .collect(Collectors.toMap(
                        row -> String.valueOf(row[0]),
                        row -> String.valueOf(row[1]),
                        (a, b) -> a
                ));

        return baseItems.stream()
                .map(item -> StatCodeMappingResponse.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .statRegulatoryCode(mappingMap.getOrDefault(item.getCode(), null))
                        .build())
                .toList();
    }
}
