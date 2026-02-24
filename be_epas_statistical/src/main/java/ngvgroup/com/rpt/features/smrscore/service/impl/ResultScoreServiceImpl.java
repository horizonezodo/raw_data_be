package ngvgroup.com.rpt.features.smrscore.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrscore.dto.*;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchGroupRepository;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchKpiRepository;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchRepository;
import ngvgroup.com.rpt.features.smrscore.service.ResultScoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultScoreServiceImpl implements ResultScoreService {
    private final SmrScoreBranchRepository repository;
    private final SmrScoreBranchKpiRepository repositoryKpi;
    private final SmrScoreBranchGroupRepository repositoryGroup;

    @Override
    public Page<BranchScoreCommonInfo> getBranchResult(String keyword, String ciId, String scoreInstanceCode, Pageable pageable) {
        return repository.getBranchResult(keyword, ciId, scoreInstanceCode, pageable);
    }

    @Override
    public List<BranchScoreCommonInfo> exportExcelBranchResult(String keyword, String ciId, String scoreInstanceCode) {
        return repository.getDataExportBranchResult(keyword, ciId, scoreInstanceCode);
    }


    @Override
    public BranchResultDto getBranchResultDetail(Long id) {
        BranchScoreCommonInfo info = repository.getDetail(id);

        List<BranchGroupInfo> groupInfoList = repositoryGroup.getList(
                info.getScoreInstantCode(),
                info.getCiId(),
                info.getCiBrId()
        );

        List<BranchGroupResultDto> groupDtoList = groupInfoList.stream()
                .map(i -> new BranchGroupResultDto(i, null))
                .toList();

        groupDtoList.forEach(group ->
                group.setKpiResultDtoList(
                        repositoryKpi.getList(
                                group.getScoreInstantCode(),
                                group.getCiId(),
                                group.getCiBrId(),
                                group.getStatScoreGroupCode()
                        )
                )
        );

        BranchResultDto result = new BranchResultDto();
        result.setInfo(info);
        result.setGroupResultDtoList(groupDtoList);

        result.setProvCode(null);

        return result;

    }

    @Override
    public Page<SmrScoreSearchDto> search(String keyword, ReqSmrScoreSearchDto dto, Pageable pageable) {
        return repository.search(keyword,
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCiId(),
                dto.getScoreInstanceCode(),
                dto.getScoreTypeCode(),
                pageable
        );
    }

    @Override
    public List<SmrScoreExportExcelDto> exportExcel(String keyword, ReqSmrScoreSearchDto dto) {
        return repository.getDataExportExcel(keyword,
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCiId(),
                dto.getScoreInstanceCode(),
                dto.getScoreTypeCode()
        );
    }
}
