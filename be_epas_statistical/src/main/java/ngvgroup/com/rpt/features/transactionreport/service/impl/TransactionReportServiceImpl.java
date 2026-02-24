package ngvgroup.com.rpt.features.transactionreport.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.core.utils.SequenceUtil;
import ngvgroup.com.rpt.features.ctgcfgresource.repository.ComCfgResourceMappingRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateSheetRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateDeadLine;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateSheet;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateDeadLineRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatusSla;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusSlaRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgWorkflow;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowTransitionRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.WorkflowTransitionRepository;
import ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto;
import ngvgroup.com.rpt.features.transactionreport.common.GetOrderedColumns;
import ngvgroup.com.rpt.features.transactionreport.dto.*;
import ngvgroup.com.rpt.features.transactionreport.mapper.RptTxnStatKpiAuditMapper;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatKpiAudit;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplate;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateKpi;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateStatus;
import ngvgroup.com.rpt.features.transactionreport.repository.*;
import ngvgroup.com.rpt.features.transactionreport.service.TransactionReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionReportServiceImpl extends BaseStoredProcedureService implements TransactionReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionReportServiceImpl.class);

    private final RptTxnStatTemplateRepository templateRepo;
    private final RptTxnStatTemplateStatusRepository statusRepo;
    private final RptTxnStatKpiAuditRepository auditRepo;
    private final RptTxnStatKpiAuditMapper auditMapper;
    private final CtgCfgStatTemplateRepository ctgCfgStatTemplateRepository;
    private final RptTxnStatTemplateKpiRepository templateKpiRepository;

    private final CtgCfgWorkflowRepository ctgCfgWorkflowRepository;
    private final CtgCfgStatRegulatoryWfRepository ctgCfgStatRegulatoryWfRepository;
    private final CtgCfgStatTemplateDeadLineRepository deadlineRepo;
    private final CtgCfgWorkflowTransitionRepository transitionRepo;
    private final CtgCfgStatusRepository ctgCfgStatusRepo;
    private final CtgCfgStatusSlaRepository statusSlaRepo;
    private final WorkflowTransitionRepository workflowTransitionRepository;
    private final CtgCfgStatTemplateKpiRepository ctgCfgStatTemplateKpiRepository;
    private final SequenceUtil sequenceUtil;
    private final StoredProcedureService procedureService;
    private final JdbcTemplate jdbcTemplate;
    private final GetOrderedColumns columns;
    private final ComCfgResourceMappingRepository resourceMappingRepository;
    private final CtgCfgStatTemplateSheetRepository ctgCfgStatTemplateSheetRepository;


    // -------------------------------------------------------------------------
    // Helper inner classes để giảm duplication
    // -------------------------------------------------------------------------

    /**
     * Context chung khi lấy trạng thái khởi tạo + SLA cho workflow
     */
    private static class WorkflowContext {
        String workflowCode;
        String currentStatusCode;
        String currentStatusName;
        String lastTransitionCode;
        String initialTransitionName;
        Timestamp startAt;
        Timestamp slaDueAt;
        Integer warningTimeInMinutes;
    }

    /**
     * Tham số tạo STATUS HISTORY, gom lại để tránh method 10+ tham số
     */
    private static class StatusCreationParams {
        String orgCode;
        String statInstanceCode;
        String templateCode;
        String transitionComment;
        String transitionCode;
        String transitionName;
        String statusCode;
        String statusName;
        Timestamp slaDueAt;
        Integer warningTimeInMinutes;
    }

    /**
     * Dùng chung cho 2 hàm searchReports & searchReportResults
     */
    private static class NormalizedFilter {
        List<String> statusCodesList;
        Date realToDate;
    }

    // -------------------------------------------------------------------------
    // 1. Tổng hợp dữ liệu báo cáo
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public List<AggregateReportResponseDto> aggregateReport(TransactionReportRequestDto req, String type) {

        Date reportDataDate = resolveReportDataDate(req);
        CtgCfgStatus statusPresent = new CtgCfgStatus();

        if (!VariableConstants.NEW.equals(type)) {
            RptTxnStatTemplate rptTxnStatTemplate =
                    templateRepo.findFirstByTemplateCodeIn(req.getTemplateCodes());
            if (rptTxnStatTemplate != null) {
                statusPresent = ctgCfgStatusRepo
                        .findByStatusCode(rptTxnStatTemplate.getCurrentStatusCode())
                        .orElse(null);
                if (statusPresent == null || statusPresent.getIsAggregation() == 0) {
                    throw new BusinessException(StatisticalErrorCode.CANNOT_AGGREGATE_DATA);
                }
            }
            return progressUpdateAggregate(req, reportDataDate, statusPresent.getStatusCode());
        }

        return progressNewAggregate(req, reportDataDate);
    }

    private WorkflowContext buildWorkflowContext(String regulatoryTypeCode, String statusCode) {
        WorkflowContext ctx = new WorkflowContext();

        String workflowCode =
                ctgCfgStatRegulatoryWfRepository.findWorkflowCodeByRegulatoryType(regulatoryTypeCode);

        CtgCfgWorkflow workflow = ctgCfgWorkflowRepository.findByWorkflowCode(workflowCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, workflowCode));

        if (statusCode == null) {
            statusCode = workflow.getInitialStatusCode();
        }
        WorkflowTransition transition = transitionRepo
                .findFirstByFromStatusCodeAndWorkflowCode(statusCode, workflowCode);

        if (transition == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, workflowCode);
        }

        ctx.workflowCode = workflowCode;
        ctx.currentStatusCode = transition.getToStatusCode();
        ctx.currentStatusName = ctgCfgStatusRepo.findStatusName(ctx.currentStatusCode);
        ctx.lastTransitionCode = transition.getTransitionCode();
        ctx.initialTransitionName = transition.getTransitionName();

        ctx.startAt = new Timestamp(System.currentTimeMillis());
        CtgCfgStatusSla statusSla = statusSlaRepo
                .findCurrentByStatusCode(ctx.currentStatusCode, new Date())
                .orElse(null);

        ctx.slaDueAt = computeDueAt(ctx.startAt, statusSla);
        ctx.warningTimeInMinutes = calculateWarningTimeInMinutes(statusSla);

        return ctx;
    }

    private List<AggregateReportResponseDto> progressNewAggregate(TransactionReportRequestDto req,
                                                                  Date reportDataDate) {

        Date now = new Date();
        WorkflowContext wfCtx = buildWorkflowContext(req.getRegulatoryTypeCode(), null);

        String makerUserCode = getCurrentUserName();
        String makerUserName = getCurrentUserFullName();

        List<AggregateReportResponseDto> listResponse = new ArrayList<>();

        for (String templateCode : req.getTemplateCodes()) {
            AggregateReportResponseDto response = aggregateSingleTemplateNew(
                    req,
                    reportDataDate,
                    now,
                    wfCtx,
                    makerUserCode,
                    makerUserName,
                    templateCode
            );
            listResponse.add(response);
        }

        return listResponse;
    }

    private AggregateReportResponseDto aggregateSingleTemplateNew(
            TransactionReportRequestDto req,
            Date reportDataDate,
            Date now,
            WorkflowContext wfCtx,
            String makerUserCode,
            String makerUserName,
            String templateCode
    ) {

        boolean exists = templateRepo.existsByOrgCodeAndTemplateCodeAndReportPeriodAndReportDataDateAndIsVoid(
                req.getOrgCode(), templateCode, req.getReportPeriod(), reportDataDate, 0
        );
        if (exists) {
            throw new BusinessException(StatisticalErrorCode.CANNOT_AGGREGATE_DATA);
        }

        CtgCfgStatTemplate cfg = ctgCfgStatTemplateRepository.findByTemplateCode(templateCode);
        if (cfg == null) {
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_NOT_FOUND);
        }

        String statInstanceCode = generateInstanceCode();
        String prevInstance = templateRepo.findPrevStatInstanceCodeOnly(
                        templateCode, req.getReportPeriod(), reportDataDate)
                .stream()
                .findFirst()
                .orElse(null);

        int revNo = 1;
        if (prevInstance != null) {
            Integer prevRev = templateRepo.findRevNoByInstanceCode(prevInstance);
            revNo = prevRev != null ? prevRev + 1 : 1;
        }

        Integer prevRunNo = templateRepo.findLatestAggregationRunNo(
                req.getOrgCode(), templateCode, req.getReportPeriod(), reportDataDate
        );
        int aggregationRunNo = (prevRunNo != null ? prevRunNo + 1 : 1);

        int isVoid = 0; // Aggregate mới luôn chưa bị void (giống logic ban đầu)

        // Insert RPT_TXN_STAT_TEMPLATE
        RptTxnStatTemplate template = new RptTxnStatTemplate();
        template.setOrgCode(req.getOrgCode());
        template.setStatInstanceCode(statInstanceCode);
        template.setPrevStatInstanceCode(prevInstance);
        template.setRevNo(revNo);
        template.setWorkflowCode(wfCtx.workflowCode);
        template.setTemplateCode(templateCode);
        template.setTemplateName(cfg.getTemplateName());
        template.setReportPeriod(req.getReportPeriod());
        template.setReportDataDate(reportDataDate);
        template.setTxnDate(new Timestamp(now.getTime()));
        template.setMakerUserCode(makerUserCode);
        template.setMakerUserName(makerUserName);
        template.setCurrentStatusCode(wfCtx.currentStatusCode);
        template.setCurrentStatusName(wfCtx.currentStatusName);
        template.setLastTransitionCode(wfCtx.lastTransitionCode);
        template.setStartAt(wfCtx.startAt);

        // SLA
        template.setSlaDueAt(wfCtx.slaDueAt);
        template.setSlaElapsedTime(wfCtx.warningTimeInMinutes); // phút
        template.setSlaResult("OK");

        template.setIsVoid(isVoid);
        template.setAggregationRunNo(aggregationRunNo);
        template.setRecordStatus(VariableConstants.DD);
        updateReportDueTime(template);
        templateRepo.save(template);

        // Insert RPT_TXN_STAT_TEMPLATE_STATUS
        StatusCreationParams params = buildStatusParams(req, template, wfCtx);
        RptTxnStatTemplateStatus status = createStatus(params);
        statusRepo.save(status);

        // Chạy procedure tổng hợp KPI
        runStatCommonKpi(templateCode,
                revNo,
                req.getOrgCode(),
                statInstanceCode,
                reportDataDate,
                aggregationRunNo,
                makerUserCode
        );

        AggregateReportResponseDto responseDto = new AggregateReportResponseDto();
        responseDto.setId(template.getId());
        responseDto.setStatInstanceCode(template.getStatInstanceCode());
        responseDto.setTemplateCode(template.getTemplateCode());
        return responseDto;
    }

    private List<AggregateReportResponseDto> progressUpdateAggregate(TransactionReportRequestDto req,
                                                                     Date reportDataDate,
                                                                     String statusCode) {

        String makerUserCode = getCurrentUserName();

        List<AggregateReportResponseDto> responseList = new ArrayList<>();

        if (req.getStatInstanceCodes() == null || req.getStatInstanceCodes().isEmpty()) {
            return responseList;
        }

        // Dùng lại logic workflow + SLA từ helper → không còn duplicated fragment
        WorkflowContext wfCtx = buildWorkflowContext(req.getRegulatoryTypeCode(), statusCode);

        List<RptTxnStatTemplate> templates =
                templateRepo.findAllByStatInstanceCodeIn(req.getStatInstanceCodes());
        List<RptTxnStatTemplateStatus> listStatus = new ArrayList<>();

        for (RptTxnStatTemplate template : templates) {
            Integer newRevNo =
                    template.getAggregationRunNo() == null ? 1 : template.getAggregationRunNo() + 1;
            template.setAggregationRunNo(newRevNo);

            StatusCreationParams params = buildStatusParams(req, template, wfCtx);
            RptTxnStatTemplateStatus status = createStatus(params);
            listStatus.add(status);

            AggregateReportResponseDto responseDto = new AggregateReportResponseDto();
            responseDto.setId(template.getId());
            responseDto.setStatInstanceCode(template.getStatInstanceCode());
            responseDto.setTemplateCode(template.getTemplateCode());
            responseList.add(responseDto);
        }

        if (!templates.isEmpty()) {
            templateRepo.saveAll(templates);
        }
        if (!listStatus.isEmpty()) {
            statusRepo.saveAll(listStatus);
        }

        for (RptTxnStatTemplate template : templates) {
            runStatCommonKpi(
                    template.getTemplateCode(),
                    template.getRevNo(),
                    template.getOrgCode(),
                    template.getStatInstanceCode(),
                    reportDataDate,
                    template.getAggregationRunNo(),
                    makerUserCode
            );
        }
        return responseList;
    }

    /**
     * Gom logic sinh params cho createStatus để tránh code trùng ở 2 luồng NEW / UPDATE
     */
    private StatusCreationParams buildStatusParams(TransactionReportRequestDto req,
                                                   RptTxnStatTemplate template,
                                                   WorkflowContext wfCtx) {
        StatusCreationParams params = new StatusCreationParams();
        params.orgCode = req.getOrgCode();
        params.statInstanceCode = template.getStatInstanceCode();
        params.templateCode = template.getTemplateCode();
        params.transitionComment = req.getTransitionComment();
        params.transitionCode = wfCtx.lastTransitionCode;
        params.transitionName = wfCtx.initialTransitionName;
        params.statusCode = wfCtx.currentStatusCode;
        params.statusName = wfCtx.currentStatusName;
        params.slaDueAt = wfCtx.slaDueAt;
        params.warningTimeInMinutes = wfCtx.warningTimeInMinutes;
        return params;
    }

    private RptTxnStatTemplateStatus createStatus(StatusCreationParams params) {
        Date now = new Date();
        String makerUserCode = getCurrentUserName();
        String makerUserName = getCurrentUserFullName();

        RptTxnStatTemplateStatus status = new RptTxnStatTemplateStatus();
        status.setOrgCode(params.orgCode);
        status.setStatInstanceCode(params.statInstanceCode);
        status.setTemplateCode(params.templateCode);
        status.setTransitionComment(params.transitionComment);
        status.setTransitionAt(new Timestamp(now.getTime()));

        status.setTransitionCode(params.transitionCode);
        status.setTransitionName(params.transitionName);

        status.setStatusCode(params.statusCode);
        status.setStatusName(params.statusName);

        status.setTxnUserId(makerUserCode);
        status.setTxnUserName(makerUserName);

        Timestamp slaActualAt = new Timestamp(now.getTime());
        status.setSlaDueAt(params.slaDueAt);
        status.setSlaActualAt(slaActualAt);
        status.setSlaElapsedTime(params.warningTimeInMinutes);
        status.setSlaStatus(determineStrictSlaStatus(params.slaDueAt, slaActualAt));

        status.setRecordStatus(VariableConstants.DD);

        return status;
    }
    
    private void runStatCommonKpi(
            String templateCode,
            Number revNo,
            String orgCode,
            String statInstanceCode,
            Date reportDataDate,
            Integer aggregationRunNo,
            String createdBy
    ) {
        String validSql = ctgCfgStatTemplateRepository.findExpressionSqlByTemplateCode(templateCode);
        if (validSql == null || validSql.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, templateCode);
        }

        List<StoredProcedureParameter> parameters = List.of(
                StoredProcedureParameter.input("p_template_code", templateCode),
                StoredProcedureParameter.input("p_rev_no", revNo),
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.input("p_stat_instance_code", statInstanceCode),
                StoredProcedureParameter.input("p_report_data_date",
                        new java.sql.Timestamp(reportDataDate.getTime())),
                StoredProcedureParameter.input("p_agg_run_no", aggregationRunNo),
                StoredProcedureParameter.input("p_created_by", createdBy)
        );

        procedureService.execute(validSql, parameters);
    }

    // -------------------------------------------------------------------------
    // 4. Search báo cáo & kết quả
    // -------------------------------------------------------------------------

    private NormalizedFilter normalizeFilter(TransactionReportFilterDto filter) {
        NormalizedFilter nf = new NormalizedFilter();

        nf.statusCodesList =
                (filter.getStatusCodes() == null || filter.getStatusCodes().isEmpty())
                        ? null
                        : filter.getStatusCodes();

        if (filter.getToDate() != null) {
            nf.realToDate = Date.from(filter.getToDate().toInstant().plus(1, ChronoUnit.DAYS));
        } else {
            nf.realToDate = null;
        }

        return nf;
    }

    private TransactionReportSearchParams buildSearchParams(
            String keyword,
            TransactionReportFilterDto filter,
            NormalizedFilter nf
    ) {
        List<String> templateGroupCodes = filter.getTemplateGroupCodes().isEmpty() ? null : filter.getTemplateGroupCodes();
        List<String> circularCodes = filter.getCircularCodes().isEmpty() ? null : filter.getCircularCodes();
        List<String> defaultCircularCodes = filter.getDefaultCircularCodes().isEmpty() ? null : filter.getDefaultCircularCodes();

        return TransactionReportSearchParams.builder()
                .orgCode(filter.getOrgCode())
                .regulatoryTypeCode(filter.getRegulatoryTypeCode())
                .reportPeriod(filter.getReportPeriod())
                .statusCodes(nf.statusCodesList)
                .statInstanceCode(filter.getStatInstanceCode())
                .templateCode(filter.getTemplateCode())
                .fromDate(filter.getFromDate())
                .toDate(nf.realToDate)
                .templateGroupCodes(templateGroupCodes)
                .circularCodes(circularCodes)
                .keyword(keyword)
                .defaultCircularCodes(defaultCircularCodes)
                .build();
    }

    @Override
    public Page<TransactionReportResponseDto> searchReports(
            String keyword,
            TransactionReportFilterDto filter,
            Pageable pageable
    ) {
        NormalizedFilter nf = normalizeFilter(filter);
        TransactionReportSearchParams params = buildSearchParams(keyword, filter, nf);

        if (params.getCircularCodes() == null && params.getTemplateGroupCodes() == null) {
            return Page.empty(pageable);
        }

        Page<TransactionReportResponseV1Dto> page =
                templateRepo.search(params, pageable);

        long now = System.currentTimeMillis();

        return page.map(v1 -> mapToDto(v1, now));
    }


    @Override
    public Page<TransactionReportResultResponseDto> searchReportResults(
            String keyword,
            TransactionReportFilterDto filter,
            Pageable pageable
    ) {
        NormalizedFilter nf = normalizeFilter(filter);

        TransactionReportSearchParams params = buildSearchParams(keyword, filter, nf);

        if (params.getCircularCodes() == null && params.getTemplateGroupCodes() == null) {
            return Page.empty(pageable);
        }

        return templateRepo.searchResult(params, pageable);
    }

    // -------------------------------------------------------------------------
    // 5. Lấy danh sách bước tiếp theo (NextStep)
    // -------------------------------------------------------------------------

    @Override
    public List<NextStepDto> getListNextStep(List<Long> ids) {
        List<RptTxnStatTemplate> templates = templateRepo.findByIdIn(ids);

        if (templates == null || templates.isEmpty()) {
            return Collections.emptyList();
        }

        RptTxnStatTemplate first = templates.get(0);
        String workflowCode = first.getWorkflowCode();
        String currentStatusCode = first.getCurrentStatusCode();

        boolean allSame = templates.stream()
                .allMatch(t ->
                        Objects.equals(t.getWorkflowCode(), workflowCode)
                                && Objects.equals(t.getCurrentStatusCode(), currentStatusCode)
                );

        if (!allSame) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_CODE_DIFFERENT);
        }

        String userId = getCurrentUserId();
        return workflowTransitionRepository.getListNextStep(workflowCode, currentStatusCode, userId);
    }

    // -------------------------------------------------------------------------
    // 6. Chuyển trạng thái (Transition)
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void doTransition(TransactionTransitionRequestDto req) {
        Date now = new Date();
        Timestamp nowTs = new Timestamp(now.getTime());

        String currentUserId = getCurrentUserName();
        String currentUserName = getCurrentUserFullName();

        List<RptTxnStatTemplate> templates = validateTransitionTargets(req);

        for (RptTxnStatTemplate template : templates) {
            applyTransitionToTemplate(req, template, now, nowTs, currentUserId, currentUserName);
        }
    }

    private void applyTransitionToTemplate(TransactionTransitionRequestDto req,
                                           RptTxnStatTemplate template,
                                           Date now,
                                           Timestamp nowTs,
                                           String currentUserId,
                                           String currentUserName) {

        String workflowCode = template.getWorkflowCode();
        String fromStatusCode = template.getCurrentStatusCode();
        String transitionCode = req.getTransitionCode();

        WorkflowTransition transition =
                resolveTransition(workflowCode, transitionCode, fromStatusCode);

        String newStatusCode = transition.getToStatusCode();
        String newStatusName = resolveStatusName(transition, newStatusCode);

        template.setStartAt(nowTs);
        CtgCfgStatusSla statusSla =
                statusSlaRepo.findCurrentByStatusCode(newStatusCode, now).orElse(null);

        Timestamp slaDueAt = computeDueAt(nowTs, statusSla);
        Integer warnBefore = calculateWarningTimeInMinutes(statusSla);


        Integer isVoid = resolveIsVoid(template.getIsVoid(), newStatusCode);

        if (VariableConstants.IS_VOID_YES.equals(isVoid)) {
            propagateVoidToResultTables(template);
        }

        template.setIsVoid(isVoid);
        template.setVoidReason(null);
        template.setLastTransitionCode(transitionCode);
        template.setCurrentStatusCode(newStatusCode);
        template.setCurrentStatusName(newStatusName);
        template.setSlaDueAt(slaDueAt);
        template.setSlaElapsedTime(warnBefore);
        template.setWarningSentAt(null);
        template.setEscalatedAt(null);
        template.setIsEscalated(null);
        updateReportDueTime(template);
        templateRepo.save(template);

        RptTxnStatTemplateStatus status = buildStatusHistory(
                template,
                req.getTransitionComment(),
                transition,
                nowTs,
                currentUserId,
                currentUserName,
                slaDueAt
        );
        statusRepo.save(status);
    }

    private WorkflowTransition resolveTransition(String workflowCode,
                                                 String transitionCode,
                                                 String fromStatusCode) {
        WorkflowTransition transition =
                transitionRepo.findFirstByWorkflowCodeAndTransitionCodeAndFromStatusCode(
                        workflowCode, transitionCode, fromStatusCode);

        if (transition == null) {
            throw new BusinessException(
                    ErrorCode.NOT_FOUND
            );
        }
        return transition;
    }

    private String resolveStatusName(WorkflowTransition transition, String newStatusCode) {
        String newStatusName = transition.getToStatusName();
        if (newStatusName == null || newStatusName.isBlank()) {
            newStatusName = ctgCfgStatusRepo.findStatusName(newStatusCode);
        }
        return newStatusName;
    }

    private Integer resolveIsVoid(Integer currentIsVoid, String newStatusCode) {
        CtgCfgStatus cfgStatus = ctgCfgStatusRepo.findByStatusCode(newStatusCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, newStatusCode));

        Integer isVoid = currentIsVoid;
        Integer isFinal = cfgStatus.getIsFinal();

        if (isFinal != null) {
            if (VariableConstants.IS_FINAL_SUCCESS.equals(isFinal)) {
                isVoid = VariableConstants.IS_VOID_NO;
            } else if (VariableConstants.IS_FINAL_VOID.equals(isFinal) || VariableConstants.IS_FINAL_REJECT.equals(isFinal)){
                isVoid = VariableConstants.IS_VOID_YES;
            }
        }

        return isVoid;
    }

    private RptTxnStatTemplateStatus buildStatusHistory(RptTxnStatTemplate template,
                                                        String transitionComment,
                                                        WorkflowTransition transition,
                                                        Timestamp nowTs,
                                                        String currentUserId,
                                                        String currentUserName,
                                                        Timestamp slaDueAt) {

        RptTxnStatTemplateStatus status = new RptTxnStatTemplateStatus();
        status.setOrgCode(template.getOrgCode());
        status.setStatInstanceCode(template.getStatInstanceCode());
        status.setTemplateCode(template.getTemplateCode());
        status.setTransitionComment(transitionComment);

        status.setTransitionAt(nowTs);
        status.setTransitionCode(template.getLastTransitionCode());
        status.setTransitionName(transition.getTransitionName());
        status.setStatusCode(template.getCurrentStatusCode());
        status.setStatusName(template.getCurrentStatusName());
        status.setTxnUserId(currentUserId);
        status.setTxnUserName(currentUserName);

        status.setSlaDueAt(slaDueAt);
        status.setSlaActualAt(nowTs);
        status.setSlaStatus(determineStrictSlaStatus(slaDueAt, nowTs));
        status.setSlaElapsedTime(null);
        status.setWarningSentAt(null);
        status.setEscalatedAt(null);
        status.setIsEscalated(0);
        status.setRecordStatus(VariableConstants.DD);

        return status;
    }

    private List<RptTxnStatTemplate> validateTransitionTargets(TransactionTransitionRequestDto req) {
        List<RptTxnStatTemplate> templates = templateRepo.findByIdIn(req.getIds());
        if (templates == null || templates.isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.NOT_EXISTS);
        }

        String firstWorkflowCode = templates.get(0).getWorkflowCode();
        String firstStatusCode = templates.get(0).getCurrentStatusCode();

        boolean allSame = templates.stream()
                .allMatch(t -> firstWorkflowCode.equals(t.getWorkflowCode())
                        && firstStatusCode.equals(t.getCurrentStatusCode()));

        if (!allSame) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_CODE_DIFFERENT);
        }

        return templates;
    }

    // -------------------------------------------------------------------------
    // 7. Helper chung
    // -------------------------------------------------------------------------

    private Date resolveReportDataDate(TransactionReportRequestDto req) {
        LocalDate d = switch (req.getReportPeriod()) {
            case VariableConstants.RPT_PERIOD_YEAR -> LocalDate.of(req.getYear(), 12, 31);
            case VariableConstants.RPT_PERIOD_QUARTER -> {
                int quarter = Integer.parseInt(req.getPrecious().replace("Q", ""));
                int lastMonth = quarter * 3;
                LocalDate tmp = LocalDate.of(req.getYear(), lastMonth, 1);
                yield tmp.withDayOfMonth(tmp.lengthOfMonth());
            }
            case VariableConstants.RPT_PERIOD_MONTH -> {
                LocalDate tmp = LocalDate.of(req.getYear(), req.getMonth(), 1);
                yield tmp.withDayOfMonth(tmp.lengthOfMonth());
            }
            case VariableConstants.RPT_PERIOD_DAY -> LocalDate.of(req.getYear(), req.getMonth(), req.getDay());
            default -> LocalDate.now();
        };
        return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Sinh mã STAT_INSTANCE_CODE theo quy tắc:
     * RPT.yyMMdd.xxxxx
     */
    public synchronized String generateInstanceCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        return sequenceUtil.getNextSequence(
                "RPT",
                "RPT_TXN_STAT_TEMPLATE",
                "STAT_INSTANCE_CODE",
                datePart,
                1,
                5
        );
    }

    /**
     * REPORT_DUE_TIME = REPORT_DATA_DATE + DEADLINE_VALUE + DEADLINE_DAY_TIME.
     */
    private void updateReportDueTime(RptTxnStatTemplate template) {
        CtgCfgStatTemplateDeadLine deadline = getCurrentDeadline(template);
        if (deadline == null) {
            return;
        }

        Date reportDataDate = template.getReportDataDate();
        if (reportDataDate == null) {
            return;
        }

        // Ngày cơ sở + số ngày cộng thêm
        LocalDateTime dueDateTime = buildBaseDueDateTime(deadline, reportDataDate);

        // Áp dụng DEADLINE_DAY_TIME (có thể là "HH:mm" hoặc số giờ)
        dueDateTime = applyDeadlineTime(
                deadline.getDeadlineDayTime(),
                dueDateTime,
                template.getTemplateCode()
        );

        template.setReportDueTime(Timestamp.valueOf(dueDateTime));
    }

    /**
     * Lấy cấu hình deadline hiện hành cho template.
     */
    private CtgCfgStatTemplateDeadLine getCurrentDeadline(RptTxnStatTemplate template) {
        return deadlineRepo
                .findCurrentByTemplateCode(template.getTemplateCode())
                .orElse(null);
    }

    /**
     * Tính LocalDateTime cơ sở = REPORT_DATA_DATE + DEADLINE_VALUE (ngày).
     */
    private LocalDateTime buildBaseDueDateTime(CtgCfgStatTemplateDeadLine deadline,
                                               Date reportDataDate) {

        LocalDate baseDate = reportDataDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        long plusDays = Long.parseLong(deadline.getDeadlineValue());
        return baseDate.atStartOfDay().plusDays(plusDays);
    }

    /**
     * Áp dụng DEADLINE_DAY_TIME vào dueDateTime.
     * - Nếu null/blank: trả lại dueDateTime gốc.
     * - Nếu dạng "HH:mm": set giờ & phút.
     * - Nếu là số: cộng thêm số giờ.
     */
    private LocalDateTime applyDeadlineTime(String deadlineDayTime,
                                            LocalDateTime dueDateTime,
                                            String templateCode) {
        if (deadlineDayTime == null || deadlineDayTime.isBlank()) {
            return dueDateTime;
        }

        String timeStr = deadlineDayTime.trim();
        if (timeStr.contains(":")) {
            return applyHourMinute(timeStr, dueDateTime, templateCode);
        }

        return applyPlusHours(timeStr, dueDateTime, templateCode);
    }

    /**
     * Dạng "HH:mm".
     */
    private LocalDateTime applyHourMinute(String timeStr,
                                          LocalDateTime dueDateTime,
                                          String templateCode) {
        try {
            String[] parts = timeStr.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;

            return dueDateTime.withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0);
        } catch (Exception e) {
            LOGGER.warn("Failed to parse DEADLINE_DAY_TIME '{}' for template {}",
                    timeStr, templateCode, e);
            return dueDateTime; // lỗi thì vẫn dùng giờ cũ
        }
    }

    /**
     * Dạng số giờ (vd "8").
     */
    private LocalDateTime applyPlusHours(String timeStr,
                                         LocalDateTime dueDateTime,
                                         String templateCode) {
        try {
            long plusHours = Long.parseLong(timeStr);
            return dueDateTime.plusHours(plusHours);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid hour value '{}' in DEADLINE_DAY_TIME for template {}",
                    timeStr, templateCode, e);
            return dueDateTime; // lỗi thì vẫn dùng giờ cũ
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveKpiAudit(TransactionTransitionRequestDto req) {
        if (req.getKpiAudits() == null || req.getKpiAudits().isEmpty()) {
            return;
        }

        Timestamp nowTs = new Timestamp(System.currentTimeMillis());

        // 1. Lưu Audit Log (Giữ nguyên để tra cứu lịch sử)
        List<RptTxnStatKpiAudit> audits = auditMapper.toEntityListFromRequest(req.getKpiAudits());
        audits.forEach(audit -> {
            audit.setTxnUserId(getCurrentUserName());
            audit.setTxnUserName(getCurrentUserFullName());
            audit.setChangedAt(nowTs);
            audit.setRecordStatus(VariableConstants.DD);
        });
        auditRepo.saveAll(audits);

        // 2. Lọc lấy giá trị mới nhất cho từng KPI (theo statInstanceCode + kpiCode)
        List<KpiAuditRequestDto> uniqueAuditList = getLatestAuditPerKpi(req.getKpiAudits());

        // 3. Update bảng RPT_TXN_STAT_TEMPLATE_KPI
        updateTemplateKpiVersioning(uniqueAuditList);

        // 4. Update bảng động (Dynamic Table)
        updateDynamicReportTableSimple(uniqueAuditList);
    }

    /**
     * Lấy bản ghi cuối cùng cho mỗi cặp (statInstanceCode, kpiCode)
     */
    private List<KpiAuditRequestDto> getLatestAuditPerKpi(List<KpiAuditRequestDto> kpiAudits) {
        if (kpiAudits == null || kpiAudits.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, KpiAuditRequestDto> latestAuditsMap = new HashMap<>();
        for (KpiAuditRequestDto dto : kpiAudits) {
            String key = dto.getStatInstanceCode() + "_" + dto.getKpiCode();
            latestAuditsMap.put(key, dto);
        }
        return new ArrayList<>(latestAuditsMap.values());
    }

    // ======================================================================
    // 3. UPDATE VERSIONING CHO BẢNG RPT_TXN_STAT_TEMPLATE_KPI
    // ======================================================================
    private void updateTemplateKpiVersioning(List<KpiAuditRequestDto> uniqueAuditList) {
        if (uniqueAuditList == null || uniqueAuditList.isEmpty()) {
            return;
        }

        Map<String, List<KpiAuditRequestDto>> auditsByInstance = uniqueAuditList.stream()
                .collect(Collectors.groupingBy(KpiAuditRequestDto::getStatInstanceCode));

        List<RptTxnStatTemplateKpi> entitiesToSave = new ArrayList<>();

        for (Map.Entry<String, List<KpiAuditRequestDto>> entry : auditsByInstance.entrySet()) {
            String instanceCode = entry.getKey();
            List<KpiAuditRequestDto> instanceAudits = entry.getValue();

            Set<String> kpiCodes = instanceAudits.stream()
                    .map(KpiAuditRequestDto::getKpiCode)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (kpiCodes.isEmpty()) {
                continue;
            }

            Map<String, String> newValueMap = instanceAudits.stream()
                    .filter(a -> a.getKpiCode() != null)
                    .collect(Collectors.toMap(
                            KpiAuditRequestDto::getKpiCode,
                            KpiAuditRequestDto::getKpiNewValue,
                            (v1, v2) -> v2
                    ));

            List<RptTxnStatTemplateKpi> activeKpis =
                    templateKpiRepository.findActiveKpis(instanceCode, new ArrayList<>(kpiCodes));

            for (RptTxnStatTemplateKpi oldKpi : activeKpis) {
                oldKpi.setIsActive(0);
                entitiesToSave.add(oldKpi);

                RptTxnStatTemplateKpi newKpi = new RptTxnStatTemplateKpi();
                BeanUtils.copyProperties(oldKpi, newKpi);

                newKpi.setId(null);
                newKpi.setIsActive(1);
                newKpi.setKpiValue(newValueMap.get(oldKpi.getKpiCode()));

                Integer revNo = newKpi.getRevNo();
                newKpi.setRevNo(revNo == null ? 1 : revNo + 1);

                entitiesToSave.add(newKpi);
            }
        }

        if (!entitiesToSave.isEmpty()) {
            templateKpiRepository.saveAll(entitiesToSave);
        }
    }

    // ======================================================================
    // 4. UPDATE BẢNG ĐỘNG (DYNAMIC TABLE) – VERSION ĐƠN GIẢN
    // ======================================================================
    private void updateDynamicReportTableSimple(List<KpiAuditRequestDto> uniqueAuditList) {
        if (uniqueAuditList == null || uniqueAuditList.isEmpty()) {
            return;
        }

        Set<String> templateCodes = uniqueAuditList.stream()
                .map(KpiAuditRequestDto::getTemplateCode)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.toSet());

        Set<String> templateKpiCodes = uniqueAuditList.stream()
                .map(KpiAuditRequestDto::getTemplateKpiCode)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.toSet());

        if (templateCodes.isEmpty() || templateKpiCodes.isEmpty()) {
            return;
        }

        List<DynamicTableRoutingDto> routingConfigs =
                ctgCfgStatTemplateKpiRepository.findTableMapping(
                        new ArrayList<>(templateCodes),
                        new ArrayList<>(templateKpiCodes)
                );

        if (routingConfigs == null || routingConfigs.isEmpty()) {
            return;
        }

        Map<String, String> routingLookup = routingConfigs.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getTemplateCode() + "_" + dto.getTargetKpiCode(),
                        DynamicTableRoutingDto::getTargetTableName,
                        (v1, v2) -> v1
                ));

        Map<String, List<KpiAuditRequestDto>> tableBatchMap = new HashMap<>();

        for (KpiAuditRequestDto audit : uniqueAuditList) {
            String key = audit.getTemplateCode() + "_" + audit.getTemplateKpiCode();
            String targetTable = routingLookup.get(key);
            if (targetTable == null) {
                continue;
            }

            tableBatchMap
                    .computeIfAbsent(targetTable, k -> new ArrayList<>())
                    .add(audit);
        }

        if (tableBatchMap.isEmpty()) {
            return;
        }

        String makerUserCode = getCurrentUserName();
        for (Map.Entry<String, List<KpiAuditRequestDto>> entry : tableBatchMap.entrySet()) {
            String tableName = entry.getKey();
            List<KpiAuditRequestDto> batchItems = entry.getValue();
            executeBatchUpdateJDBC(tableName, batchItems, makerUserCode);
        }
    }

    // ======================================================================
    // 5. HELPER: EXECUTE JDBC BATCH UPDATE
    // ======================================================================
    private static final Set<String> SPECIAL_COLUMNS = Set.of(
            "ID", "KPI_VALUE", "CREATED_BY", "CREATED_DATE", "IS_ACTIVE", "REV_NO"
    );

    private void executeBatchUpdateJDBC(String tableName,
                                        List<KpiAuditRequestDto> batchItems,
                                        String makerUserCode) {

        if (batchItems == null || batchItems.isEmpty()) {
            return;
        }

        List<String> allColumns = columns.getOrderedColumns(tableName);
        if (allColumns.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Metadata not found for table: " + tableName
            );
        }

        List<String> fixedColumns = allColumns.stream()
                .filter(col -> !SPECIAL_COLUMNS.contains(col))
                .toList();

        String insertColsStr =
                String.join(", ", fixedColumns)
                        + ", KPI_VALUE, CREATED_BY, CREATED_DATE, IS_ACTIVE, REV_NO";

        String selectColsStr =
                String.join(", ", fixedColumns)
                        + ", ?, ?, SYSDATE, 1, REV_NO + 1";

        String insertSql = """
                INSERT INTO %s (%s)
                SELECT %s FROM %s
                WHERE STAT_INSTANCE_CODE = ? AND KPI_CODE = ? AND IS_ACTIVE = 1
                """.formatted(tableName, insertColsStr, selectColsStr, tableName);

        String deactivateSql = """
                UPDATE %s t1
                SET IS_ACTIVE = 0
                WHERE STAT_INSTANCE_CODE = ? AND KPI_CODE = ? AND IS_ACTIVE = 1
                  AND ID < (
                        SELECT MAX(ID)
                        FROM %s t2
                        WHERE t2.STAT_INSTANCE_CODE = t1.STAT_INSTANCE_CODE
                          AND t2.KPI_CODE = t1.KPI_CODE
                    )
                """.formatted(tableName, tableName);

        try {
            jdbcTemplate.batchUpdate(insertSql, batchItems, batchItems.size(),
                    (PreparedStatement ps, KpiAuditRequestDto item) -> {
                        ps.setString(1, item.getKpiNewValue());
                        ps.setString(2, makerUserCode);
                        ps.setString(3, item.getStatInstanceCode());
                        ps.setString(4, item.getTemplateKpiCode());
                    });

            jdbcTemplate.batchUpdate(deactivateSql, batchItems, batchItems.size(),
                    (PreparedStatement ps, KpiAuditRequestDto item) -> {
                        ps.setString(1, item.getStatInstanceCode());
                        ps.setString(2, item.getTemplateKpiCode());
                    });

        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Error updating dynamic table " + tableName + ": " + e.getMessage()
            );
        }
    }

    // -------------------------------------------------------------------------
    // Helper Methods cho SLA
    // -------------------------------------------------------------------------

    private static Timestamp computeDueAt(Timestamp startAt, CtgCfgStatusSla sla) {
        if (sla == null || startAt == null || sla.getDurationTime() == null) {
            return startAt;
        }

        // Gọi hàm toMillis để quy đổi DURATION_TIME dựa trên TIME_UNIT
        long addMillis = toMillis(sla.getDurationTime(), sla.getTimeUnit());

        return new Timestamp(startAt.getTime() + addMillis);
    }

    private static Integer calculateWarningTimeInMinutes(CtgCfgStatusSla sla) {
        if (sla == null || sla.getWarningBeforeTime() == null) {
            return 0;
        }

        String unit = (sla.getTimeUnit() != null) ? sla.getTimeUnit().toUpperCase() : "";

        return switch (unit) {
            // CASE: GIỜ (CM033.001) -> Nhân 60
            case VariableConstants.UNIT_CODE_HOUR,
                 VariableConstants.UNIT_TEXT_HOUR,
                 VariableConstants.UNIT_TEXT_HOURS
                    -> sla.getWarningBeforeTime() * 60;

            // CASE: PHÚT (CM033.002) -> Giữ nguyên
            case VariableConstants.UNIT_CODE_MINUTE,
                 VariableConstants.UNIT_TEXT_MINUTE,
                 VariableConstants.UNIT_TEXT_MINUTES
                    -> sla.getWarningBeforeTime();

            default -> sla.getWarningBeforeTime();
        };
    }

    private static String determineStrictSlaStatus(Timestamp dueAt, Timestamp actualAt) {
        if (dueAt == null || actualAt == null) {
            return VariableConstants.ACHIEVED;
        }

        return (actualAt.getTime() > dueAt.getTime())
                ? VariableConstants.BREACHED
                : VariableConstants.ACHIEVED;
    }

    private static long toMillis(Integer amount, String unit) {
        if (amount == null || amount <= 0) {
            return 0L;
        }
        if (unit == null) {
            unit = VariableConstants.UNIT_TEXT_HOUR;
        }

        String normalizedUnit = (unit == null || unit.isBlank())
                ? VariableConstants.UNIT_TEXT_HOUR
                : unit.trim().toUpperCase();

        return switch (normalizedUnit) {
            case VariableConstants.UNIT_TEXT_MINUTE,
                 VariableConstants.UNIT_TEXT_MINUTES,
                 VariableConstants.UNIT_CODE_MINUTE
                    -> amount.longValue() * VariableConstants.MILLIS_PER_MINUTE;

            case VariableConstants.UNIT_TEXT_HOUR,
                 VariableConstants.UNIT_TEXT_HOURS,
                 VariableConstants.UNIT_CODE_HOUR
                    -> amount.longValue() * VariableConstants.MILLIS_PER_HOUR;

            default -> amount.longValue() * VariableConstants.MILLIS_PER_HOUR;
        };
    }


    @Override
    public boolean existsByStatusCode(String statusCode, String regulatoryTypeCode) {
        if (regulatoryTypeCode != null && !regulatoryTypeCode.isEmpty()) {
            String workflowCode =
                    ctgCfgStatRegulatoryWfRepository.findWorkflowCodeByRegulatoryType(regulatoryTypeCode);

            CtgCfgWorkflow workflow = ctgCfgWorkflowRepository.findByWorkflowCode(workflowCode)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, workflowCode));

            if (statusCode == null || statusCode.isEmpty()) {
                statusCode = workflow.getInitialStatusCode();
            }
        }
        return resourceMappingRepository.existsByUserIdAndResourceCodeAndIsActive(getCurrentUserId(), statusCode, 1);
    }

    private TransactionReportResponseDto mapToDto(
            TransactionReportResponseV1Dto v1,
            long now
    ) {
        String slaStatus = calculateSlaStatus(
                v1.getSlaDueAt(),
                v1.getSlaElapsedTime(),
                now
        );

        return new TransactionReportResponseDto(
                v1.getId(),
                v1.getCurrentStatusName(),
                v1.getCurrentStatusCode(),
                v1.getTxnDate(),
                v1.getStatInstanceCode(),
                v1.getTemplateCode(),
                v1.getTemplateName(),
                v1.getTemplateGroupCode(),
                v1.getTemplateGroupName(),
                v1.getCommonName(),
                v1.getReportDataDate(),
                v1.getReportDueTime(),
                v1.getCircularName(),
                v1.getSendCount(),
                v1.getExportCount(),
                slaStatus,
                v1.getWorkflowCode(),
                v1.getReportPeriod(),
                v1.getRegulatoryTypeCode(),
                v1.getCircularCode(),
                v1.getOrgCode(),
                v1.getSlaDueAt(),
                v1.getSlaElapsedTime(),
                v1.getAggregationRunNo()
        );
    }

    private String calculateSlaStatus(
            Timestamp slaDueAt,
            Integer warningMinutes,
            long now
    ) {
        if (slaDueAt != null) {
            long dueTimeMillis = slaDueAt.getTime();
            long warningOffsetMillis =
                    (warningMinutes != null ? warningMinutes : 0) * 60_000L;

            long thresholdTimeMillis = dueTimeMillis - warningOffsetMillis;

            if (now > dueTimeMillis) {
                return VariableConstants.GUI_OVERDUE;
            } else if (now > thresholdTimeMillis) {
                return VariableConstants.GUI_NEAR_TERM;
            } else {
                return VariableConstants.GUI_IN_TERM;
            }
        }

        return VariableConstants.GUI_IN_TERM;
    }

        // thêm phương thức mới để truyền void

        /**
         * Lan truyền trạng thái IS_VOID = 1 từ bảng chính xuống các bảng kết quả chi tiết
         * khi báo cáo bị hủy (void).
         */
    private void propagateVoidToResultTables(RptTxnStatTemplate template) {

        // Lấy sheet theo templateCode
        List<CtgCfgStatTemplateSheet> sheets =
                ctgCfgStatTemplateSheetRepository.findAllByTemplateCode(template.getTemplateCode());

        if (sheets == null || sheets.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, template.getTemplateCode() );
        }

        // Lấy bảng đích trực tiếp từ TABLE_DATA
        Set<String> targetTables = sheets.stream()
                .map(CtgCfgStatTemplateSheet::getTableData)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 🔹 Update IS_VOID
        for (String table : targetTables) {
            try {
                String sql = "UPDATE " + table
                        + " SET IS_VOID = 1 WHERE STAT_INSTANCE_CODE = ?";

                jdbcTemplate.update(sql, template.getStatInstanceCode());

            } catch (Exception e) {
                LOGGER.error("Failed to propagate IS_VOID to table {} for STAT_INSTANCE_CODE={}",
                        table, template.getTemplateCode(), e);
            }
        }
    }
}
