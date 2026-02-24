package ngvgroup.com.rpt.features.smrtxnscore.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.context.AuditContext;
import ngvgroup.com.rpt.core.utils.SequenceUtil;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgStatScoreTypeWf;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgStatScoreTypeWfRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.WorkflowTransitionRepository;
import ngvgroup.com.rpt.features.smrscore.dto.*;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreCalculationRequestDto;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreRequestDto;
import ngvgroup.com.rpt.features.smrtxnscore.model.*;
import ngvgroup.com.rpt.features.smrtxnscore.repository.SmrTxnScoreRepository;
import ngvgroup.com.rpt.features.smrtxnscore.repository.SmrTxnScoreStatusRepository;
import ngvgroup.com.rpt.features.smrtxnscore.service.ScoreCalculationService;
import org.hibernate.dialect.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ngvgroup.com.rpt.core.constant.VariableConstants.DD;

@Service
@RequiredArgsConstructor
public class ScoreCalculationServiceImpl implements ScoreCalculationService {

    private final SmrTxnScoreRepository smrTxnScoreRepository;
    private final CtgCfgStatScoreTypeWfRepository ctgCfgStatScoreTypeWfRepository;
    private final StoredProcedureService storedProcedureService;
    private final SmrTxnScoreStatusRepository smrTxnScoreStatusRepository;
    private final WorkflowTransitionRepository workflowTransitionRepository;
    private static final String COL_CI_BR_ID = "CI_BR_ID";
    private static final String STAT_SCORE_GROUP_CODE = "STAT_SCORE_GROUP_CODE";
    private final JdbcTemplate jdbcTemplate;
    private final SequenceUtil sequenceUtil;

    /**
     * Tính chấm điểm cho danh sách chi nhánh (preview, chưa lưu).
     */
    @Override
    public List<BranchResultDto> calculatePreview(List<String> keywords, ScoreCalculationRequestDto req) {

        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input("p_ci_id", req.getCiId()),
                StoredProcedureParameter.input("p_stat_score_type_code", req.getStatScoreTypeCode()),
                StoredProcedureParameter.input("p_data_date_v", req.getScoreDate()),
                StoredProcedureParameter.output("o_cur", OracleTypes.CURSOR)
        );

        var result = storedProcedureService.execute("CTG_KPI_SCORE.compute_scores", params).getOutputParameters().get("o_cur");

        return mapAllBranches(result, req.getCiId(), keywords);
    }

    @SuppressWarnings("unchecked")
    public List<BranchResultDto> mapAllBranches(Object spResult, String ciId, List<String> keywords) {
        List<Map<String, Object>> rows = (List<Map<String, Object>>) spResult;
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        // --- B1: Nhóm theo chi nhánh (CI_BR_ID)
        Map<String, List<Map<String, Object>>> groupedByBranch =
                rows.stream().collect(Collectors.groupingBy(r -> (String) r.get(COL_CI_BR_ID)));

        List<BranchResultDto> branchList = new ArrayList<>();

        // --- B2: Duyệt từng chi nhánh
        for (Map.Entry<String, List<Map<String, Object>>> branchEntry : groupedByBranch.entrySet()) {
            List<Map<String, Object>> branchRows = branchEntry.getValue();
            Map<String, Object> firstRow = branchRows.get(0);

            BranchResultDto branch = new BranchResultDto();
            BranchScoreCommonInfo info = new BranchScoreCommonInfo();
            info.setCiBrId((String) firstRow.get(COL_CI_BR_ID));
            info.setCiBrName((String) firstRow.get("CI_BR_NAME"));
            info.setCiBrCode((String) firstRow.get("CI_BR_CODE"));
            info.setAchievedScore(toBigDecimal(firstRow.get("CI_BR_SCORE_VALUE")));
            info.setRankValue((String) firstRow.get("BENCHMARK_VALUE"));
            info.setRankContent((String) firstRow.get("BENCHMARK_DESC"));
            info.setScoreInstantCode((String) firstRow.get(STAT_SCORE_GROUP_CODE));
            branch.setProvCode((String) firstRow.get("PROVINCE_CODE"));
            info.setCiId(ciId);
            branch.setInfo(info);

            Map<String, List<Map<String, Object>>> groupedByGroup =
                    branchRows.stream().collect(Collectors.groupingBy(r -> (String) r.get(STAT_SCORE_GROUP_CODE)));

            List<BranchGroupResultDto> groupList = new ArrayList<>();

            for (Map.Entry<String, List<Map<String, Object>>> groupEntry : groupedByGroup.entrySet()) {
                List<Map<String, Object>> groupRows = groupEntry.getValue();
                Map<String, Object> any = groupRows.get(0);

                BranchGroupResultDto groupDto = new BranchGroupResultDto();
                BranchGroupInfo groupInfo = new BranchGroupInfo();
                groupInfo.setStatScoreGroupCode(groupEntry.getKey());
                groupInfo.setStatScoreGroupName((String) any.get("STAT_SCORE_GROUP_NAME"));
                groupInfo.setAchievedScore(toBigDecimal(any.get("GROUP_SCORE_VALUE")));
                groupInfo.setWeightScore(toBigDecimal(any.get("GROUP_WEIGHT_SCORE")));
                groupInfo.setRawScore(toBigDecimal(any.get("GROUP_RAW_SCORE")));
                groupInfo.setCiBrId((String) any.get(COL_CI_BR_ID));
                groupInfo.setScoreInstantCode((String) any.get(STAT_SCORE_GROUP_CODE));
                groupInfo.setCiId(ciId);
                groupDto.setInfo(groupInfo);
                // --- B4: Map KPI trong mỗi nhóm
                List<BranchGroupKpiResultDto> kpiList = groupRows.stream().map(r -> {
                    BranchGroupKpiResultDto kpi = new BranchGroupKpiResultDto();
                    kpi.setKpiName((String) r.get("KPI_NAME"));
                    kpi.setKpiCode((String) r.get("KPI_CODE"));
                    kpi.setKpiValue(toStringSafe(r.get("KPI_VALUE")));
                    kpi.setAchievedScore(toBigDecimal(r.get("KPI_SCORE_VALUE")));
                    kpi.setWeightScore(toBigDecimal(r.get("KPI_WEIGHT_SCORE")));
                    kpi.setRawScore(toBigDecimal(r.get("KPI_RAW_SCORE")));
                    kpi.setStatScoreGroupCode((String) r.get(STAT_SCORE_GROUP_CODE));
                    return kpi;
                }).toList();

                groupDto.setKpiResultDtoList(kpiList);
                groupList.add(groupDto);
            }

            branch.setGroupResultDtoList(groupList);
            branchList.add(branch);
        }

        if (keywords != null && !keywords.isEmpty()) {
            return branchList.stream()
                    .filter(x -> keywords.contains(x.getProvCode()))
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    private String toStringSafe(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal bigDecimal) return bigDecimal;
        try {
            return new BigDecimal(val.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }


    @Override
    @Transactional
    public void saveScore(ScoreRequestDto request, List<BranchResultDto> branchResults,
                          String makerUserCode, String makerUserName) {

        // 1️⃣ Lấy cấu hình
        CtgCfgStatScoreTypeWf statScoreType = ctgCfgStatScoreTypeWfRepository
                .findByStatScoreTypeCode(request.getStatScoreTypeCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, request.getStatScoreTypeCode()));

        List<WorkflowTransition> transitions = workflowTransitionRepository.findFirstTransition(statScoreType.getWorkflowCode());
        WorkflowTransition firstTransition = transitions.get(0);
        // 2️⃣ Lưu bản ghi SMR_TXN_SCORE
        SmrTxnScore txnScore = new SmrTxnScore();
        txnScore.setRecordStatus(DD);
        txnScore.setScoreInstanceCode(request.getScoreInstanceCode());
        txnScore.setCiId(request.getCiId());
        txnScore.setStatScoreTypeCode(request.getStatScoreTypeCode());
        txnScore.setStatScoreTypeName(request.getStatScoreTypeName());
        txnScore.setWorkflowCode(statScoreType.getWorkflowCode());
        txnScore.setVersionNo(statScoreType.getVersionNo());
        txnScore.setTxnDate(request.getTxnDate());
        txnScore.setScorePeriod(request.getScorePeriod());
        txnScore.setScoreDate(request.getScoreDate());
        txnScore.setMakerUserCode(makerUserCode);
        txnScore.setMakerUserName(makerUserName);
        txnScore.setTxnContent("Chấm điểm tự động");
        txnScore.setCurrentStatusCode(firstTransition.getToStatusCode());
        txnScore.setCurrentStatusName(firstTransition.getToStatusName());
        txnScore.setIsFinal(false);
        txnScore = smrTxnScoreRepository.save(txnScore);

        // 3️⃣ Chuẩn bị dữ liệu để batch insert
        List<SmrTxnScoreBranch> branchEntities = new ArrayList<>();
        List<SmrTxnScoreBranchGroup> groupEntities = new ArrayList<>();
        List<SmrTxnScoreBranchKpi> kpiEntities = new ArrayList<>();

        for (BranchResultDto branch : branchResults) {
            SmrTxnScoreBranch branchEntity = mapTxnScoreBranch(request, branch, txnScore);
            branchEntities.add(branchEntity);

            if (branch.getGroupResultDtoList() != null) {
                for (BranchGroupResultDto group : branch.getGroupResultDtoList()) {
                    SmrTxnScoreBranchGroup groupEntity = mapTxnScoreBranchGroup(request, branch, group, txnScore);
                    groupEntities.add(groupEntity);

                    if (group.getKpiResultDtoList() != null) {
                        for (BranchGroupKpiResultDto kpi : group.getKpiResultDtoList()) {
                            SmrTxnScoreBranchKpi kpiEntity = mapTxnScoreBranchKpi(request, branch, group, kpi, txnScore);
                            kpiEntities.add(kpiEntity);
                        }
                    }
                }
            }
        }

        // 4️⃣ Batch insert bằng JdbcTemplate
        AuditContext audit = new AuditContext(); // sinh 1 lần
        batchInsertBranch(branchEntities, audit);
        batchInsertGroup(groupEntities, audit);
        batchInsertKpi(kpiEntities, audit);

        SmrTxnScoreStatus log = new SmrTxnScoreStatus();
        log.setScoreInstanceCode(txnScore.getScoreInstanceCode());
        log.setStatusCode(txnScore.getCurrentStatusCode());
        log.setStatusName(txnScore.getCurrentStatusName());
        log.setTransitionAt(new Timestamp(System.currentTimeMillis()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.setTxnUserId(auth.getName());
        log.setTxnUserName(auth.getName());
        log.setRecordStatus(DD);
        log.setTransitionCode(firstTransition.getTransitionCode());
        log.setTransitionName(firstTransition.getTransitionName());
        smrTxnScoreStatusRepository.save(log);
    }

    private void batchInsertBranch(List<SmrTxnScoreBranch> branches, AuditContext audit) {
        if (branches.isEmpty()) return;

        String sql = """
                    INSERT INTO SMR_TXN_SCORE_BRANCH (
                        RECORD_STATUS, IS_ACTIVE, SCORE_INSTANCE_CODE, CI_ID, CI_BR_ID,
                        STAT_INSTANCE_CODE, TXN_DATE, SCORE_PERIOD, SCORE_DATE,
                        ACHIEVED_SCORE, RANK_VALUE, RANK_CONTENT,
                        CREATED_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY,
                        MODIFIED_DATE, MODIFIED_BY, IS_DELETE
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, branches, 1000, (ps, entity) -> {
            int i = 1;
            ps.setString(i++, entity.getRecordStatus());
            ps.setInt(i++, entity.getIsActive());
            ps.setString(i++, entity.getScoreInstanceCode());
            ps.setString(i++, entity.getCiId());
            ps.setString(i++, entity.getCiBrId());
            ps.setString(i++, entity.getStatInstanceCode());
            ps.setDate(i++, java.sql.Date.valueOf(entity.getTxnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            ps.setString(i++, entity.getScorePeriod());
            ps.setDate(i++, java.sql.Date.valueOf(entity.getScoreDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            ps.setBigDecimal(i++, entity.getAchievedScore());
            ps.setString(i++, entity.getRankValue() != null && !entity.getRankValue().isBlank()
                    ? entity.getRankValue()
                    : "N/A");
            ps.setString(i++, entity.getRankContent() != null && !entity.getRankContent().isBlank()
                    ? entity.getRankContent()
                    : "N/A");
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setInt(i++, 0);
        });
    }

    private void batchInsertGroup(List<SmrTxnScoreBranchGroup> groups, AuditContext audit) {
        if (groups.isEmpty()) return;

        String sql = """
                    INSERT INTO SMR_TXN_SCORE_BRANCH_GROUP (
                        RECORD_STATUS, IS_ACTIVE, SCORE_INSTANCE_CODE, STAT_SCORE_GROUP_CODE,
                        STAT_SCORE_GROUP_NAME, TXN_DATE, SCORE_PERIOD, SCORE_DATE,
                        RAW_SCORE, WEIGHT_SCORE, ACHIEVED_SCORE, CI_ID, CI_BR_ID,
                        CREATED_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY,
                        MODIFIED_DATE, MODIFIED_BY, IS_DELETE
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, groups, 1000, (ps, entity) -> {
            int i = 1;
            ps.setString(i++, entity.getRecordStatus());
            ps.setInt(i++, entity.getIsActive());
            ps.setString(i++, entity.getScoreInstanceCode());
            ps.setString(i++, entity.getStatScoreGroupCode());
            ps.setString(i++, entity.getStatScoreGroupName());
            ps.setDate(i++, java.sql.Date.valueOf(entity.getTxnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            ps.setString(i++, entity.getScorePeriod());
            ps.setDate(i++, java.sql.Date.valueOf(entity.getScoreDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            ps.setBigDecimal(i++, entity.getRawScore());
            ps.setBigDecimal(i++, entity.getWeightScore());
            ps.setBigDecimal(i++, entity.getAchievedScore());
            ps.setString(i++, entity.getCiId());
            ps.setString(i++, entity.getCiBrId());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setInt(i++, 0);
        });
    }

    private void batchInsertKpi(List<SmrTxnScoreBranchKpi> kpis, AuditContext audit) {
        if (kpis.isEmpty()) return;

        String sql = """
                    INSERT INTO SMR_TXN_SCORE_BRANCH_KPI (
                        RECORD_STATUS, IS_ACTIVE, SCORE_INSTANCE_CODE, STAT_SCORE_GROUP_CODE,
                        STAT_SCORE_GROUP_NAME, KPI_CODE, KPI_NAME, KPI_VALUE,
                        SCORE_PERIOD, SCORE_DATE, RAW_SCORE, WEIGHT_SCORE, ACHIEVED_SCORE,
                        CI_ID, CI_BR_ID, CREATED_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY,
                        MODIFIED_DATE, MODIFIED_BY, IS_DELETE
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, kpis, 1000, (ps, entity) -> {
            int i = 1;
            ps.setString(i++, entity.getRecordStatus());
            ps.setInt(i++, entity.getIsActive());
            ps.setString(i++, entity.getScoreInstanceCode());
            ps.setString(i++, entity.getStatScoreGroupCode());
            ps.setString(i++, entity.getStatScoreGroupName());
            ps.setString(i++, entity.getKpiCode());
            ps.setString(i++, entity.getKpiName());
            ps.setString(i++, entity.getKpiValue());
            ps.setString(i++, entity.getScorePeriod());
            ps.setDate(i++, java.sql.Date.valueOf(entity.getScoreDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            ps.setBigDecimal(i++, entity.getRawScore());
            ps.setBigDecimal(i++, entity.getWeightScore());
            ps.setBigDecimal(i++, entity.getAchievedScore());
            ps.setString(i++, entity.getCiId());
            ps.setString(i++, entity.getCiBrId());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setTimestamp(i++, audit.getNow());
            ps.setString(i++, audit.getUsername());
            ps.setInt(i++, 0);
        });
    }


    private SmrTxnScoreBranch mapTxnScoreBranch(ScoreRequestDto request, BranchResultDto branch, SmrTxnScore txnScore) {
        SmrTxnScoreBranch entity = new SmrTxnScoreBranch();
        entity.setRecordStatus(DD);
        entity.setIsActive(1);
        entity.setScoreInstanceCode(txnScore.getScoreInstanceCode());
        entity.setCiId(request.getCiId());
        entity.setCiBrId(branch.getInfo().getCiBrId());
        entity.setStatInstanceCode(request.getStatScoreTypeCode());
        entity.setTxnDate(request.getTxnDate());
        entity.setScorePeriod(request.getScorePeriod());
        entity.setScoreDate(request.getScoreDate());
        entity.setAchievedScore(defaultIfNull(branch.getInfo().getAchievedScore()));
        entity.setRankValue(branch.getInfo().getRankValue());
        entity.setRankContent(branch.getInfo().getRankContent());
        return entity;
    }

    private SmrTxnScoreBranchGroup mapTxnScoreBranchGroup(ScoreRequestDto request, BranchResultDto branch,
                                                          BranchGroupResultDto group, SmrTxnScore txnScore) {
        SmrTxnScoreBranchGroup entity = new SmrTxnScoreBranchGroup();
        entity.setRecordStatus(DD);
        entity.setIsActive(1);
        entity.setScoreInstanceCode(txnScore.getScoreInstanceCode());
        entity.setStatScoreGroupCode(group.getStatScoreGroupCode());
        entity.setStatScoreGroupName(group.getInfo().getStatScoreGroupName());
        entity.setTxnDate(request.getTxnDate());
        entity.setScorePeriod(request.getScorePeriod());
        entity.setScoreDate(request.getScoreDate());
        entity.setRawScore(defaultIfNull(group.getInfo().getRawScore()));
        entity.setWeightScore(defaultIfNull(group.getInfo().getWeightScore()));
        entity.setAchievedScore(defaultIfNull(group.getInfo().getAchievedScore()));
        entity.setCiId(request.getCiId());
        entity.setCiBrId(branch.getInfo().getCiBrId());
        return entity;
    }

    private SmrTxnScoreBranchKpi mapTxnScoreBranchKpi(ScoreRequestDto request, BranchResultDto branch,
                                                      BranchGroupResultDto group, BranchGroupKpiResultDto kpi,
                                                      SmrTxnScore txnScore) {
        SmrTxnScoreBranchKpi entity = new SmrTxnScoreBranchKpi();
        entity.setRecordStatus(DD);
        entity.setIsActive(1);
        entity.setScoreInstanceCode(txnScore.getScoreInstanceCode());
        entity.setStatScoreGroupCode(kpi.getStatScoreGroupCode());
        entity.setStatScoreGroupName(group.getInfo().getStatScoreGroupName());
        entity.setKpiCode(kpi.getKpiCode());
        entity.setKpiName(kpi.getKpiName());
        entity.setKpiValue(kpi.getKpiValue() != null ? kpi.getKpiValue() : "");
        entity.setScorePeriod(request.getScorePeriod());
        entity.setScoreDate(request.getScoreDate());
        entity.setRawScore(defaultIfNull(kpi.getRawScore()));
        entity.setAchievedScore(defaultIfNull(kpi.getAchievedScore()));
        entity.setWeightScore(defaultIfNull(kpi.getWeightScore()));
        entity.setCiId(request.getCiId());
        entity.setCiBrId(branch.getInfo().getCiBrId());
        return entity;
    }

    private BigDecimal defaultIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }


    private static final String TABLE_NAME = "SMR_TXN_SCORE";
    private static final String COLUMN_NAME = "SCORE_INSTANCE_CODE";

    @Override
    @Transactional
    public synchronized String generateScoreInstanceCode() {

        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String prefix = "SCR";

        return sequenceUtil.getNextSequence(
                prefix,        // p_prefix
                TABLE_NAME,    // p_table_name
                COLUMN_NAME,   // p_column_name
                datePart,      // p_date_text (để reset theo ngày)
                1,             // số lượng cần sinh
                5              // chiều dài padding (00001)
        );
    }
}
