package ngvgroup.com.rpt.features.ctgcfgstat.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscoregroupkpi.CtgCfgStatScoreGroupKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstat.mapper.CtgCfgStatScoreGroupKpiMapper;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreGroupKpi;
import ngvgroup.com.rpt.features.ctgcfgstat.repository.CtgCfgStatScoreGroupKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatScoreGroupKpiService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ngvgroup.com.rpt.core.constant.VariableConstants.DD;

@Service
@AllArgsConstructor
public class CtgCfgStatScoreGroupKpiServiceImpl implements CtgCfgStatScoreGroupKpiService {

    private final CtgCfgStatScoreGroupKpiRepository ctgCfgStatScoreGroupKpiRepository;

    @Override
    public void create(List<CtgCfgStatScoreGroupKpiDto> dtos) {

        if(dtos.isEmpty()){
            return;
        }
        List<CtgCfgStatScoreGroupKpi> existingEntities = ctgCfgStatScoreGroupKpiRepository.findAllByStatScoreGroupCode(dtos.get(0).getStatScoreGroupCode());


        List<CtgCfgStatScoreGroupKpi> newEntities = dtos.stream()
                .map(dto -> {
                    CtgCfgStatScoreGroupKpi entity = CtgCfgStatScoreGroupKpiMapper.INSTANCE.toEntity(dto);
                    entity.setOrgCode(VariableConstants.ORG);
                    entity.setRecordStatus(DD);
                    return entity;
                })
                .toList();


        Function<CtgCfgStatScoreGroupKpi, String> keyFunc = e -> e.getKpiCode() + "::" + e.getStatScoreGroupCode();

        Map<String, CtgCfgStatScoreGroupKpi> existingMap = existingEntities.stream()
                .collect(Collectors.toMap(keyFunc, e -> e));

        Map<String, CtgCfgStatScoreGroupKpi> newMap = newEntities.stream()
                .collect(Collectors.toMap(keyFunc, e -> e));


        List<CtgCfgStatScoreGroupKpi> toInsert = newEntities.stream()
                .filter(e -> !existingMap.containsKey(keyFunc.apply(e)))
                .toList();
        ctgCfgStatScoreGroupKpiRepository.saveAll(toInsert);


        List<CtgCfgStatScoreGroupKpi> toUpdate = newEntities.stream()
                .filter(e -> existingMap.containsKey(keyFunc.apply(e)))
                .map(e -> {
                    CtgCfgStatScoreGroupKpi existing = existingMap.get(keyFunc.apply(e));
                    existing.setKpiCode(e.getKpiCode());
                    existing.setStatScoreGroupCode(e.getStatScoreGroupCode());
                    existing.setWeightScore(e.getWeightScore());
                    existing.setSortNumber(e.getSortNumber());
                    existing.setOrgCode(e.getOrgCode());
                    return existing;
                })
                .toList();
        ctgCfgStatScoreGroupKpiRepository.saveAll(toUpdate);


        List<CtgCfgStatScoreGroupKpi> toDelete = existingEntities.stream()
                .filter(e -> !newMap.containsKey(keyFunc.apply(e)))
                .toList();
        ctgCfgStatScoreGroupKpiRepository.deleteAll(toDelete);
    }

    @Override
    public void delete(String statScoreGroupCode){
        List<CtgCfgStatScoreGroupKpi> entities = ctgCfgStatScoreGroupKpiRepository
                .findAllByStatScoreGroupCode(statScoreGroupCode);
        if (!entities.isEmpty()) {
            ctgCfgStatScoreGroupKpiRepository.deleteAll(entities);
        }
    }

    @Override
    public List<CtgCfgStatScoreGroupKpiDto> getAllByStatScoreGroupCode(String statScoreGroupCode){
        return CtgCfgStatScoreGroupKpiMapper.INSTANCE.toListDto(ctgCfgStatScoreGroupKpiRepository.findAllByStatScoreGroupCode(statScoreGroupCode));
    }

}
