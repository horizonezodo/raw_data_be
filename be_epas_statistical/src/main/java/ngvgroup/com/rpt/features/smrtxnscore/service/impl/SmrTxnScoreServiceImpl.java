package ngvgroup.com.rpt.features.smrtxnscore.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.WorkflowTransitionRepository;
import ngvgroup.com.rpt.features.smrscore.dto.BranchGroupInfo;
import ngvgroup.com.rpt.features.smrscore.dto.BranchGroupResultDto;
import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;
import ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo;
import ngvgroup.com.rpt.features.smrscore.mapper.SmrScoreBranchGroupMapper;
import ngvgroup.com.rpt.features.smrscore.mapper.SmrScoreBranchKpiMapper;
import ngvgroup.com.rpt.features.smrscore.mapper.SmrScoreBranchMapper;
import ngvgroup.com.rpt.features.smrscore.mapper.SmrScoreMapper;
import ngvgroup.com.rpt.features.smrscore.model.SmrScore;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranch;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchGroup;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchKpi;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchGroupRepository;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchKpiRepository;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreBranchRepository;
import ngvgroup.com.rpt.features.smrscore.repository.SmrScoreRepository;
import ngvgroup.com.rpt.features.smrtxnscore.dto.*;
import ngvgroup.com.rpt.features.smrtxnscore.mapper.SmrTxnScoreMapper;
import ngvgroup.com.rpt.features.smrtxnscore.model.*;
import ngvgroup.com.rpt.features.smrtxnscore.repository.*;
import ngvgroup.com.rpt.features.smrtxnscore.service.SmrTxnScoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmrTxnScoreServiceImpl implements SmrTxnScoreService {
    private final SmrTxnScoreMapper mapper;

    private final WorkflowTransitionRepository workflowTransitionRepository;
    private final CtgCfgStatusRepository ctgCfgStatusRepository;
    private final SmrTxnScoreStatusRepository smrTxnScoreStatusRepository;

    private final SmrTxnScoreBranchGroupRepository smrTxnScoreBranchGroupRepository;
    private final SmrTxnScoreRepository repo;
    private final SmrTxnScoreBranchKpiRepository smrTxnScoreBranchKpiRepository;
    private final SmrTxnScoreBranchRepository smrTxnScoreBranchRepository;

    private final SmrScoreBranchGroupRepository smrScoreBranchGroupRepository;
    private final SmrScoreBranchRepository smrScoreBranchRepository;
    private final SmrScoreBranchKpiRepository smrScoreBranchKpiRepository;
    private final SmrScoreRepository smrScoreRepository;

    private final SmrScoreMapper smrScoreMapper;
    private final SmrScoreBranchMapper smrScoreBranchMapper;
    private final SmrScoreBranchKpiMapper smrScoreBranchKpiMapper;
    private final SmrScoreBranchGroupMapper smrScoreBranchGroupMapper;

    @Override
    public void createScore(SmrTxnScoreDTO dto) {
        SmrTxnScore score = mapper.toEntity(dto);
        score.setRecordStatus(VariableConstants.DD);
        repo.save(score);
    }

    @Override
    public Page<SmrTxnScorePageDTO> pageScore(String keyword, SearchFilterDTO dto, Pageable pageable) {
        List<String> statusCodesList = null;
        if (dto.getStatusCodes() != null && !dto.getStatusCodes().isEmpty()) {
            statusCodesList = dto.getStatusCodes();
        }
        return repo.pageScore(keyword, dto.getStartDate(), dto.getEndDate(), dto.getCiCode(), dto.getScoreInstanceCode(), statusCodesList, pageable);
    }

    @Override
    public List<SmrTxnScoreExportExcelDTO> exportExcel(String keyword, SearchFilterDTO dto) {
        List<String> statusCodesList = null;
        if (dto.getStatusCodes() != null && !dto.getStatusCodes().isEmpty()) {
            statusCodesList = dto.getStatusCodes();
        }
        return repo.exportData(keyword, dto.getStartDate(), dto.getEndDate(), dto.getCiCode(), dto.getScoreInstanceCode(), statusCodesList);
    }

    @Override
    public SmrTxnScoreDetailDTO getDetailScore(String scoreInstanceCode) {
        return this.repo.getDetail(scoreInstanceCode);
    }

    @Override
    public List<NextStepDto> getNextSteps(Long id) {
        SmrTxnScore score = repo.findByIdAndIsFinal(id, false);
        if (score == null) throw new BusinessException(StatisticalErrorCode.SCORE_DTO_IS_NULL);

        return workflowTransitionRepository
                .getByWorkflowCode(score.getWorkflowCode(), score.getCurrentStatusCode());
    }


    @Override
    public void changeStatus(Long id, ChangeStatusDto req) {
        SmrTxnScore score = repo.findByIdAndIsFinal(id, false);
        if (score == null) throw new BusinessException(StatisticalErrorCode.SCORE_DTO_IS_NULL);

        WorkflowTransition transition = workflowTransitionRepository
                .findByWorkflowCodeAndToStatusCodeAndFromStatusCode(score.getWorkflowCode(), req.getTransitionCode(), score.getCurrentStatusCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        repo.save(score);

        CtgCfgStatus nextStatus = ctgCfgStatusRepository.findByStatusCode(transition.getToStatusCode())
                .orElseThrow(() -> new BusinessException(StatisticalErrorCode.STATUS_NOT_FOUND));

        score.setCurrentStatusCode(transition.getToStatusCode());
        score.setCurrentStatusName(transition.getToStatusName());
        score.setIsFinal(nextStatus.getIsFinal() == 1);
        repo.save(score);

        SmrTxnScoreStatus log = getSmrTxnScoreStatus(req, score, transition);
        smrTxnScoreStatusRepository.save(log);

        if (nextStatus.getIsFinal() == 1) {
            insertFinalResult(score);
        }

    }

    private static SmrTxnScoreStatus getSmrTxnScoreStatus(ChangeStatusDto req, SmrTxnScore score, WorkflowTransition transition) {
        SmrTxnScoreStatus log = new SmrTxnScoreStatus();
        log.setScoreInstanceCode(score.getScoreInstanceCode());
        log.setStatusCode(transition.getToStatusCode());
        log.setStatusName(transition.getToStatusName());
        log.setTransitionComment(req.getComment());
        log.setTransitionAt(new Timestamp(new Date().getTime()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.setTxnUserId(auth.getName());
        log.setTxnUserName(auth.getName());
        log.setRecordStatus(VariableConstants.DD);
        log.setTransitionCode(transition.getTransitionCode());
        log.setTransitionName(transition.getTransitionName());
        return log;
    }

    private void insertFinalResult(SmrTxnScore score) {
        // 1. Insert SMR_SCORE
        SmrScore smrScore = smrScoreMapper.toSmrScore(score);
        smrScore.setId(null);
        smrScoreRepository.save(smrScore);

        // 2. Insert SMR_SCORE_BRANCH
        List<SmrScoreBranch> branches = buildBranchData(score);
        smrScoreBranchRepository.saveAll(branches);

        // 3. Insert SMR_SCORE_BRANCH_GROUP
        List<SmrScoreBranchGroup> groups = buildGroupData(score);
        smrScoreBranchGroupRepository.saveAll(groups);

        // 4. Insert SMR_SCORE_BRANCH_KPI
        List<SmrScoreBranchKpi> kpis = buildKpiData(score);
        smrScoreBranchKpiRepository.saveAll(kpis);
    }

    private List<SmrScoreBranch> buildBranchData(SmrTxnScore score) {
        List<SmrTxnScoreBranch> branches = smrTxnScoreBranchRepository.findAllByScoreInstanceCode(score.getScoreInstanceCode());
        return smrScoreBranchMapper.toSmrScoreBranchList(branches);
    }

    private List<SmrScoreBranchGroup> buildGroupData(SmrTxnScore score) {
        List<SmrTxnScoreBranchGroup> branchGroups = smrTxnScoreBranchGroupRepository.findAllByScoreInstanceCode(score.getScoreInstanceCode());
        return smrScoreBranchGroupMapper.toSmrScoreBranchList(branchGroups);
    }

    private List<SmrScoreBranchKpi> buildKpiData(SmrTxnScore score) {
        List<SmrTxnScoreBranchKpi> branchKpis = smrTxnScoreBranchKpiRepository.findAllByScoreInstanceCode(score.getScoreInstanceCode());
        return smrScoreBranchKpiMapper.toSmrScoreBranchList(branchKpis);
    }

    @Override
    public Page<BranchScoreCommonInfo> getBranch(String keyword, String ciId, String scoreInstanceCode, Pageable pageable) {
        return smrTxnScoreBranchRepository.getBranch(keyword, ciId, scoreInstanceCode, pageable);
    }

    @Override
    public List<BranchScoreCommonInfo> exportExcelTxnScoreBranch(String keyword, String ciId, String scoreInstanceCode) {
        return smrTxnScoreBranchRepository.getDataExportBranch(keyword, ciId, scoreInstanceCode);
    }

    @Override
    public BranchResultDto getBranchDetail(Long id) {
        BranchScoreCommonInfo infoData = smrTxnScoreBranchRepository.getDetail(id);
        BranchResultDto branchDto = new BranchResultDto();
        branchDto.setInfo(infoData);
        List<BranchGroupInfo> groupInfoList =
                smrTxnScoreBranchGroupRepository.getList(
                        branchDto.getInfo().getScoreInstantCode(),
                        branchDto.getInfo().getCiId(),
                        branchDto.getInfo().getCiBrId()
                );
        List<BranchGroupResultDto> groupDtoList = groupInfoList.stream()
                .map(info -> new BranchGroupResultDto(info, null))
                .toList();
        groupDtoList.forEach(group ->
                group.setKpiResultDtoList(
                        smrTxnScoreBranchKpiRepository.getList(
                                group.getScoreInstantCode(),
                                group.getCiId(),
                                group.getCiBrId(),
                                group.getStatScoreGroupCode()
                        )
                )
        );
        branchDto.setGroupResultDtoList(groupDtoList);
        return branchDto;
    }


}
