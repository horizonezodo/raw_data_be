package ngvgroup.com.bpm.features.com_txn_task_inbox.service.impl;


import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.common.util.DateUtils;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureResponse;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.*;
import ngvgroup.com.bpm.features.com_txn_task_inbox.mapper.ComTxnTaskInboxMapper;
import ngvgroup.com.bpm.features.com_txn_task_inbox.repository.ComTxnTaskInboxRepository;
import ngvgroup.com.bpm.features.common.feign.UsersFeignClient;
import ngvgroup.com.bpm.features.common.model.ComCfgParameter;
import ngvgroup.com.bpm.features.common.repository.ComCfgParameterRepository;
import ngvgroup.com.bpm.features.com_txn_task_inbox.service.ComTxnTaskInboxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComTxnTaskInboxServiceImpl implements ComTxnTaskInboxService {

    private final ComTxnTaskInboxRepository comTxnTaskInboxRepository;

    private final ComCfgParameterRepository comCfgParameterRepository;
    private final StoredProcedureService storedProcedureService;
    private final ComTxnTaskInboxMapper comTxnTaskInboxMapper;
    private final UsersFeignClient usersFeignClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SP_GET_TASKS_ASSIGNED_TO_USER = "SP_GET_TASKS_ASSIGNED_TO_USER";
    private static final String PARAM_FILTER_DATE = "p_filter_date";
    private static final String PARAM_ORG_CODE = "p_org_code";
    private static final String CUR_ASSIGNED = "cur_assigned";
    private static final String CUR_UNASSIGNED = "cur_unassigned";
    private static final String NOT_STARTED = "not_started";
    private static final String IN_PROGRESS = "in_progress";
    private static final String COMPLETED = "completed";
    private static final String OVERDUE = "overdue";
    private static final String SLA_OVERDUE_DAYS = "SLA_OVERDUE_DAYS";
    private static final String ERROR_OUTPUT_CURSOR_FROM_SP_RESPONSE =
            "Error processing output cursor from StoredProcedureResponse: {}";

    @Override
    public List<TaskListDto> getTaskList(String username, String ruleCode, List<String> listBusinessStatus,
                                         String filterDate, String orgCode, Integer slaOverdueDays) {
        // Lấy username hiện tại nếu không truyền
        String currentUser = Optional.ofNullable(username)
                .filter(u -> !u.isBlank())
                .orElse(getCurrentUsername());

        if (currentUser == null || currentUser.isEmpty()) {
            return List.of();
        }

        // Gọi repository để lấy tất cả kết quả (không phân trang)
        List<Map<String, Object>> resultList;
        boolean hasStatus = listBusinessStatus != null && !listBusinessStatus.isEmpty();
        if (hasStatus) {
            resultList = comTxnTaskInboxRepository
                    .findTasksByUserStatusRuleDateOrgWithStatus(
                            currentUser, listBusinessStatus, ruleCode, filterDate, orgCode, slaOverdueDays);
        } else {
            resultList = comTxnTaskInboxRepository
                    .findTasksByUserStatusRuleDateOrgWithoutStatus(
                            currentUser, ruleCode, filterDate, orgCode, slaOverdueDays);
        }

        // Thu thập tất cả assigneeUsername duy nhất
        Set<String> assigneeUsernames = resultList.stream()
                .map(map -> (String) map.get("assignTo"))
                .filter(Objects::nonNull)
                .filter(assigneeUsername -> !assigneeUsername.isEmpty())
                .collect(Collectors.toSet());

        // Gọi 1 lần lấy toàn bộ user info
        Map<String, UserDTO> userMap = new HashMap<>();
        if (!assigneeUsernames.isEmpty()) {
            Optional.ofNullable(usersFeignClient.getAll(new ArrayList<>(assigneeUsernames)))
                    .map(ResponseData::getData)
                    .ifPresent(users ->
                            users.forEach(user ->
                                    userMap.put(user.getUsername(), user)
                            )
                    );
        }

        // Khi map từng task

        return resultList.stream().map(map -> {
            TaskListDto dto = new TaskListDto();
            dto.setProcessInstanceCode((String) map.get("processInstanceCode"));
            dto.setProcessTypeName((String) map.get("processTypeName"));
            dto.setTaskDefineName((String) map.get("taskDefineName"));
            dto.setSlaMaxDuration(
                    map.get("slaMaxDuration") != null ? ((Number) map.get("slaMaxDuration")).intValue() : null);
            dto.setAcceptedDate(formatTimestamp(map.get("acceptedDate")));
            dto.setSlaTaskDeadline(formatTimestamp(map.get("slaTaskDeadline")));
            String assigneeUsername = (String) map.get("assignTo");
            UserDTO user = userMap.get(assigneeUsername);
            if (user != null) {
                dto.setAssignee(user.getFirstName() + " " + user.getLastName());
            } else {
                dto.setAssignee(assigneeUsername);
            }
            dto.setBusinessStatus(mapBusinessStatus((String) map.get("businessStatus")));
            return dto;
        }).toList();
    }

    private String formatTimestamp(Object timestamp) {
        return timestamp instanceof Timestamp ts
                ? FORMATTER.format(ts.toLocalDateTime())
                : null;
    }

    private String mapBusinessStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> "Đang thực hiện";
            case "COMPLETE" -> "Hoàn thành";
            case "UNASSIGNED" -> "Chưa thực hiện";
            default -> status;
        };
    }

    @Override
    public Page<InboundTransactionsDto> inboundTransactions(InOutTransDto dto, Pageable pageable) {
        return comTxnTaskInboxRepository.listInboundTrans(
                dto.getProcessTypeCode(), dto.getProcessInstanceCode(),
                dto.getCustomerCode(), dto.getOrgCode(), dto.getFromDate(), dto.getToDate(), pageable);
    }

    @Override
    public Page<OutboundTransactionsDto> outboundTransactions(InOutTransDto dto, Pageable pageable) {
        return comTxnTaskInboxRepository.listOutboundTrans(
                dto.getProcessTypeCode(), dto.getProcessInstanceCode(),
                dto.getCustomerCode(), dto.getOrgCode(), dto.getFromDate(), dto.getToDate(), pageable);
    }

    @Override
    public Page<AssignedTaskDto> getTasksAssignedToCurrentUser(String orgCode, Pageable pageable) {
        String username = getCurrentUsername();
        String fullname = getCurrentFullName();
        if (username == null) {
            return Page.empty(pageable);
        }

        // Gọi stored procedure
        List<Map<String, Object>> resultList = callProcedureGetTasksAssignedToUser(username, orgCode);

        // Convert to DTOs
        List<AssignedTaskDto> dtos = resultList.stream()
                .map(map -> new AssignedTaskDto(
                        (String) map.get("PROCESS_INSTANCE_CODE"),
                        (String) map.get("TASK_DEFINE_NAME"),
                        fullname))
                .toList();

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());

        if (start > dtos.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, dtos.size());
        }

        List<AssignedTaskDto> pageContent = dtos.subList(start, end);
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    public List<Map<String, Object>> callProcedureGetTasksAssignedToUser(String currentUser, String orgCode) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input("p_current_user", currentUser),
                StoredProcedureParameter.input(PARAM_ORG_CODE, orgCode),
                StoredProcedureParameter.output("cur_result", Types.REF_CURSOR));

        StoredProcedureResponse response = storedProcedureService.execute(SP_GET_TASKS_ASSIGNED_TO_USER, params);

        if (response == null || !response.isSuccess()) {
            log.error("Failed to execute {} procedure", SP_GET_TASKS_ASSIGNED_TO_USER);
            return Collections.emptyList();
        }

        try {
            Map<String, Object> outputParams = response.getOutputParameters();
            if (outputParams == null) {
                return Collections.emptyList();
            }
            return convertOutputCursorToListOfMap(outputParams.get("cur_result"));
        } catch (Exception e) {
            log.error("Error processing output cursor from SP_GET_TASKS_ASSIGNED_TO_USER: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public Page<TxnTaskInboxSummaryDto> getTaskSummary(String filterDate, String orgCode, Pageable pageable) {
        StoredProcedureResponse response = callProcedureGetTaskSummary(filterDate, orgCode);
        if (response == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<Map<String, Object>> curAssigned;
        List<Map<String, Object>> curUnassigned;

        try {
            Map<String, Object> outputParams = response.getOutputParameters();
            if (outputParams == null) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            curAssigned = convertOutputCursorToListOfMap(outputParams.get(CUR_ASSIGNED));
            curUnassigned = convertOutputCursorToListOfMap(outputParams.get(CUR_UNASSIGNED));
        } catch (Exception e) {
            log.error(ERROR_OUTPUT_CURSOR_FROM_SP_RESPONSE, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return processTask(curAssigned, curUnassigned, pageable);
    }

    private List<Map<String, Object>> convertOutputCursorToListOfMap(Object cursor) {
        if (cursor instanceof List<?>) {
            return ((List<?>) cursor).stream()
                    .filter(Map.class::isInstance)
                    .map(item -> (Map<String, Object>) item)
                    .toList();
        }
        return Collections.emptyList();
    }

    private Page<TxnTaskInboxSummaryDto> processTask(List<Map<String, Object>> curAssigned,
            List<Map<String, Object>> curUnassigned, Pageable pageable) {
        List<TxnTaskInboxSummaryDto> summaryList = new ArrayList<>();
        curAssigned.forEach(item -> summaryList.add(convertToDto(item, true)));
        curUnassigned.forEach(item -> summaryList.add(convertToDto(item, false)));

        return new PageImpl<>(summaryList, pageable, summaryList.size());
    }

    private TxnTaskInboxSummaryDto convertToDto(Map<String, Object> itemMap, boolean isAssigned) {
        TxnTaskInboxSummaryDto dto = new TxnTaskInboxSummaryDto();
        String assigneeUsername = (String) itemMap.get("ASSIGNEE");

        if (isAssigned && assigneeUsername != null && !assigneeUsername.isEmpty()) {
            ResponseData<List<UserDTO>> userList = usersFeignClient.getAll(List.of(assigneeUsername));

            if (Objects.nonNull(userList) && userList.getData() != null && !userList.getData().isEmpty()) {
                UserDTO user = userList.getData().get(0);
                dto.setAssignee(user.getFirstName() + " " + user.getLastName());
            } else {
                dto.setAssignee(assigneeUsername);
            }
        } else {
            dto.setAssignee(assigneeUsername);
        }

        int notStarted = convertObjectToInt(itemMap.get("NOT_STARTED"));
        int inProgress = convertObjectToInt(itemMap.get("IN_PROGRESS"));
        int completed = convertObjectToInt(itemMap.get("COMPLETED"));
        String ruleCode = (String) itemMap.get("RULE_CODE");

        dto.setAssigneeRuleCode(ruleCode);
        dto.setAssigneeUsername(assigneeUsername);
        dto.setNotStarted(notStarted);
        dto.setInProgress(inProgress);
        dto.setCompleted(completed);
        dto.setTotal(notStarted + inProgress + completed);
        dto.setIsAssigned(isAssigned);

        return dto;
    }

    private int convertObjectToInt(Object obj) {
        if (obj instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public StoredProcedureResponse callProcedureGetTaskSummary(String filterDate, String orgCode) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input(PARAM_FILTER_DATE, filterDate),
                StoredProcedureParameter.input(PARAM_ORG_CODE, orgCode),
                StoredProcedureParameter.output(CUR_ASSIGNED, Types.REF_CURSOR),
                StoredProcedureParameter.output(CUR_UNASSIGNED, Types.REF_CURSOR));
        return storedProcedureService.execute("SP_GET_TASK_SUMMARY", params);
    }

    @Transactional
    @Override
    public Page<TxnTaskInboxSummaryDto> getTaskOverdue(String orgCode, Pageable pageable) {
        Integer slaOverdueDays = getSlaOverdueDays();
        StoredProcedureResponse response = callProcedureGetTaskOverdue(orgCode, slaOverdueDays);

        List<Map<String, Object>> curAssigned = new ArrayList<>();
        List<Map<String, Object>> curUnassigned = new ArrayList<>();

        if (response != null) {
            try {
                Map<String, Object> outputParams = response.getOutputParameters();
                if (outputParams != null) {
                    curAssigned = convertOutputCursorToListOfMap(outputParams.get(CUR_ASSIGNED));
                    curUnassigned = convertOutputCursorToListOfMap(outputParams.get(CUR_UNASSIGNED));
                }
            } catch (Exception e) {
                log.error(ERROR_OUTPUT_CURSOR_FROM_SP_RESPONSE, e.getMessage(), e);
            }
        }

        return processTask(curAssigned, curUnassigned, pageable);
    }

    private Integer getSlaOverdueDays() {
        try {
            String slaOverdueValue = comCfgParameterRepository
                    .findByParamCodeAndIsActiveTrueAndIsDeleteFalse(SLA_OVERDUE_DAYS)
                    .map(ComCfgParameter::getParamValue)
                    .orElse(null);
            return slaOverdueValue != null ? Integer.parseInt(slaOverdueValue) : null;
        } catch (NumberFormatException ex) {
            log.error("{} is not a valid number: {}", SLA_OVERDUE_DAYS, ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            log.error("Error retrieving {}: {}", SLA_OVERDUE_DAYS, ex.getMessage(), ex);
            return null;
        }
    }

    public StoredProcedureResponse callProcedureGetTaskOverdue(String orgCode, Integer slaOverdueDays) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input(PARAM_ORG_CODE, orgCode),
                StoredProcedureParameter.input("p_sla_overdue_days", slaOverdueDays),
                StoredProcedureParameter.output(CUR_ASSIGNED, Types.REF_CURSOR),
                StoredProcedureParameter.output(CUR_UNASSIGNED, Types.REF_CURSOR));
        return storedProcedureService.execute("SP_GET_TASK_OVERDUE", params);
    }

    @Transactional
    @Override
    public TxnTaskStatusDto getTaskStatus(String filterDate, String orgCode) {
        StoredProcedureResponse response = callProcedureGetTaskStatus(filterDate, orgCode);
        return parseTaskStatusResponse(response);
    }

    public StoredProcedureResponse callProcedureGetTaskStatus(String filterDate, String orgCode) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input(PARAM_FILTER_DATE, filterDate),
                StoredProcedureParameter.input(PARAM_ORG_CODE, orgCode),
                StoredProcedureParameter.output(NOT_STARTED, Types.INTEGER),
                StoredProcedureParameter.output(IN_PROGRESS, Types.INTEGER),
                StoredProcedureParameter.output(COMPLETED, Types.INTEGER),
                StoredProcedureParameter.output(OVERDUE, Types.INTEGER));
        return storedProcedureService.execute("SP_GET_TASK_STATUS", params);
    }

    @Transactional
    @Override
    public TxnTaskStatusDto getTaskPercentage(String filterDate, String orgCode) {
        StoredProcedureResponse response = callProcedureGetTaskPercentage(filterDate, orgCode);
        return parseTaskStatusResponse(response);
    }

    public StoredProcedureResponse callProcedureGetTaskPercentage(String filterDate, String orgCode) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input(PARAM_FILTER_DATE, filterDate),
                StoredProcedureParameter.input(PARAM_ORG_CODE, orgCode),
                StoredProcedureParameter.output(NOT_STARTED, Types.INTEGER),
                StoredProcedureParameter.output(IN_PROGRESS, Types.INTEGER),
                StoredProcedureParameter.output(COMPLETED, Types.INTEGER),
                StoredProcedureParameter.output(OVERDUE, Types.INTEGER));
        return storedProcedureService.execute("SP_GET_TASK_PERCENTAGE", params);
    }

    private TxnTaskStatusDto parseTaskStatusResponse(StoredProcedureResponse response) {
        TxnTaskStatusDto statusDto = new TxnTaskStatusDto();
        if (response != null) {
            try {
                Map<String, Object> outputParams = response.getOutputParameters();
                if (outputParams != null) {
                    statusDto.setNotStarted((Integer) outputParams.getOrDefault(NOT_STARTED, 0));
                    statusDto.setInProgress((Integer) outputParams.getOrDefault(IN_PROGRESS, 0));
                    statusDto.setCompleted((Integer) outputParams.getOrDefault(COMPLETED, 0));
                    statusDto.setOverdue((Integer) outputParams.getOrDefault(OVERDUE, 0));
                }
            } catch (Exception e) {
                log.error("Error parsing output params from StoredProcedureResponse: {}", e.getMessage(), e);
            }
        }
        return statusDto;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }
        return null;
    }

    private String getCurrentFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("given_name") + " " + jwt.getClaimAsString("family_name");
        }
        return null;
    }

    @Override
    public Page<ComTxnTaskInboxResponse> searchTaskList(String filter, Pageable pageRequest) {
        String finalFilter = filter != null ? '%' + filter.toLowerCase() + '%' : null;
        Page<ActHiTaskIntResponse> responses = comTxnTaskInboxRepository.searchTaskList(finalFilter, pageRequest);
        return responses.map(item -> {
            ComTxnTaskInboxResponse response = comTxnTaskInboxMapper.toResponse(item);
            response.setCreatedDate(DateUtils.formatDateTime(item.getCreatedDate()));
            response.setTaskUpdateTime(DateUtils.formatDateTime(item.getTaskUpdateTime()));
            return response;
        });
    }

    @Override
    public Page<ComTxnTaskInboxResponse> searchAdvanceTaskList(ComTxnTaskInboxSearch request, Pageable pageRequest) {
        Page<ActHiTaskIntResponse> responses = comTxnTaskInboxRepository.searchTaskListAdvance(request, pageRequest);

        return responses.map(item -> {
            ComTxnTaskInboxResponse response = comTxnTaskInboxMapper.toResponse(item);
            response.setCreatedDate(DateUtils.formatDateTime(item.getCreatedDate()));
            response.setTaskUpdateTime(DateUtils.formatDateTime(item.getTaskUpdateTime()));
            return response;
        });
    }

    @Override
    public void changeIsSuspend(Integer isSuspended, String taskId) {
        BpmTxnTaskInbox entity = comTxnTaskInboxRepository.findByTaskIdAndIsDelete(taskId, 0)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, taskId));
        entity.setIsSuspend(isSuspended);
        comTxnTaskInboxRepository.save(entity);
    }

}
