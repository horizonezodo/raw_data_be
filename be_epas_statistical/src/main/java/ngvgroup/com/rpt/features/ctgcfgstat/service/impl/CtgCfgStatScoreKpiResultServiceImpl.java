package ngvgroup.com.rpt.features.ctgcfgstat.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto;
import ngvgroup.com.rpt.features.ctgcfgstat.mapper.CtgCfgStatScoreKpiResultMapper;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreKpiResult;
import ngvgroup.com.rpt.features.ctgcfgstat.repository.CtgCfgStatScoreKpiResultRepository;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatScoreKpiResultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CtgCfgStatScoreKpiResultServiceImpl implements CtgCfgStatScoreKpiResultService {
    private final CtgCfgStatScoreKpiResultRepository ctgCfgStatScoreKpiResultRepository;

    @Override
    public Page<CtgCfgStatScoreKpiResultDto> searchAll(String kpiCode,String keyword, Pageable pageable){
        return ctgCfgStatScoreKpiResultRepository.searchAll(kpiCode,keyword,pageable);
    }


    @Override
    @Transactional
    public void create(List<CtgCfgStatScoreKpiResultDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) return;


        String kpiCode = dtoList.get(0).getKpiCode();

        // Lấy danh sách entity hiện có trong DB theo KPI code
        List<CtgCfgStatScoreKpiResult> existingList =
                ctgCfgStatScoreKpiResultRepository.getAllByKpiCode(kpiCode);

        // Tập hợp ID hiện có trong DTO (để kiểm tra phần nào cần xóa)
        Set<Long> dtoIds = dtoList.stream()
                .map(CtgCfgStatScoreKpiResultDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // === 1️⃣ Xóa những entity trong DB mà không còn trong DTO ===
        List<CtgCfgStatScoreKpiResult> toDelete = existingList.stream()
                .filter(e -> !dtoIds.contains(e.getId()))
                .toList();
        if (!toDelete.isEmpty()) {
            ctgCfgStatScoreKpiResultRepository.deleteAll(toDelete);
        }

        // === 2️⃣ Tạo hoặc cập nhật ===
        for (CtgCfgStatScoreKpiResultDto dto : dtoList) {
            dto.setOrgCode(VariableConstants.ORG);
            dto.setRecordStatus(VariableConstants.DD);
            CtgCfgStatScoreKpiResult entity;
            if (dto.getId() != null) {
                // Cập nhật
                entity = existingList.stream()
                        .filter(e -> e.getId().equals(dto.getId()))
                        .findFirst()
                        .orElse(new CtgCfgStatScoreKpiResult());
            } else {
                // Tạo mới
                entity = new CtgCfgStatScoreKpiResult();
            }

            // Map dữ liệu từ DTO sang Entity
            dto.setRecordStatus(VariableConstants.DD);
            dto.setIsActive(1);
            CtgCfgStatScoreKpiResultMapper.INSTANCE.updateEntityFromDto(dto, entity);
            ctgCfgStatScoreKpiResultRepository.save(entity);
        }
    }


    @Override
    public CtgCfgStatScoreKpiResultDto getDetail(Long id){
        Optional<CtgCfgStatScoreKpiResult> ctgCfgStatScoreKpiResultOptional=ctgCfgStatScoreKpiResultRepository.findById(id);
        if(!ctgCfgStatScoreKpiResultOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return CtgCfgStatScoreKpiResultMapper.INSTANCE.toDto(ctgCfgStatScoreKpiResultOptional.get());
    }

    @Override
    public void delete(String kpiCode){
        ctgCfgStatScoreKpiResultRepository.deleteAllByKpiCode(kpiCode);
    }

}
