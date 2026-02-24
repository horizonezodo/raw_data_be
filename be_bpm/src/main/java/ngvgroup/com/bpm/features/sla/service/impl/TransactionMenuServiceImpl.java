package ngvgroup.com.bpm.features.sla.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.features.common.service.ComCfgBusModuleService;
import ngvgroup.com.bpm.features.sla.dto.CtgCfgProcessTypeDto;
import ngvgroup.com.bpm.features.sla.dto.TransactionMenuDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgProcessType;
import ngvgroup.com.bpm.features.sla.repository.ComCfgProcessTypeRepository;
import ngvgroup.com.bpm.features.sla.service.TransactionMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionMenuServiceImpl implements TransactionMenuService {
    private final ComCfgProcessTypeRepository comCfgProcessTypeRepository;
    private final ComCfgBusModuleService comCfgBusModuleService;
    private final ObjectMapper objectMapper;

    @Override
    public List<TransactionMenuDto> getTransactionMenu() {
        List<ngvgroup.com.bpm.features.sla.model.ComCfgProcessType> allProcessTypes = comCfgProcessTypeRepository
                .findAll().stream()
                .filter(pt -> pt.getIsDelete() == 0 && pt.getModuleCode() != null)
                .toList();

        Set<String> moduleCodes = allProcessTypes.stream()
                .map(ngvgroup.com.bpm.features.sla.model.ComCfgProcessType::getModuleCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (moduleCodes.isEmpty()) {
            return Collections.emptyList();
        }

        ResponseEntity<Object> moduleResponse = comCfgBusModuleService.getAll();
        Map<String, String> moduleCodeToNameMap = new HashMap<>();

        try {
            Object body = moduleResponse.getBody();
            if (body instanceof ResponseData<?> responseData && responseData.getData() != null) {

                List<Map<String, Object>> modules = objectMapper.convertValue(
                        responseData.getData(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );

                for (Map<String, Object> module : modules) {
                    Object codeObj = module.get("moduleCode");
                    if (codeObj instanceof String code && moduleCodes.contains(code)) {

                        Object nameObj = module.get("moduleName");
                        moduleCodeToNameMap.put(code,
                                nameObj instanceof String name ? name : "");

                    }
                }
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse module response", e);
        }


        Map<String, List<CtgCfgProcessTypeDto>> processTypesByModule = allProcessTypes.stream()
                .collect(Collectors.groupingBy(
                        ComCfgProcessType::getModuleCode,
                        Collectors.mapping(
                                pt -> new CtgCfgProcessTypeDto(pt.getProcessTypeCode(), pt.getProcessTypeName()),
                                Collectors.toList())));

        return moduleCodes.stream()
                .filter(moduleCodeToNameMap::containsKey)
                .map(moduleCode -> {
                    TransactionMenuDto dto = new TransactionMenuDto();
                    dto.setModuleCode(moduleCode);
                    dto.setModuleName(moduleCodeToNameMap.getOrDefault(moduleCode, ""));
                    dto.setProcessTypes(processTypesByModule.getOrDefault(moduleCode, Collections.emptyList()));
                    return dto;
                })
                .filter(dto -> !dto.getProcessTypes().isEmpty())
                .sorted(Comparator.comparing(TransactionMenuDto::getModuleCode))
                .collect(Collectors.toList());
    }
}
