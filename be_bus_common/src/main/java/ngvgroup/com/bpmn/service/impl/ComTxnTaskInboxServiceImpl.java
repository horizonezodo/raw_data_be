package ngvgroup.com.bpmn.service.impl;

import com.ngvgroup.bpm.core.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpmn.dto.TxnTaskInbox.*;
import ngvgroup.com.bpmn.dto.user.UserDTO;
import ngvgroup.com.bpmn.mapper.ruleMapper.ComTxnTaskInboxMapper;
import ngvgroup.com.bpmn.model.ComTxnTaskInbox;
import ngvgroup.com.bpmn.openfeign.UsersFeignClient;
import ngvgroup.com.bpmn.repository.ComTxnTaskInboxRepository;
import ngvgroup.com.bpmn.service.ComCfgParameterService;
import ngvgroup.com.bpmn.service.ComTxnTaskInboxService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngvgroup.bpm.core.dto.ResponseData;
import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.dto.StoredProcedureResponse;
import com.ngvgroup.bpm.core.service.StoredProcedureService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;

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
    private final ComCfgParameterService comCfgParameterService;
    private final StoredProcedureService storedProcedureService;
    private final ComTxnTaskInboxMapper comTxnTaskInboxMapper;
    private final UsersFeignClient usersFeignClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<TaskListDto> getTaskList(String username, String ruleCode, List<String> listBusinessStatus,
            String filterDate, String orgCode, Integer slaOverdueDays) {
        // Lấy username hiện tại nếu không truyền
        String currentUser = (username != null && !username.isEmpty()) ? username : getCurrentUsername();
        if (currentUser == null || currentUser.isEmpty()) {
            return List.of();
        }

        // Build status list filter - sử dụng trực tiếp listBusinessStatus nếu có
        List<String> statusList = (listBusinessStatus != null && !listBusinessStatus.isEmpty()) ? listBusinessStatus
                : null;

        // Gọi repository để lấy tất cả kết quả (không phân trang)
        List<Map<String, Object>> resultList;
        if (statusList != null && !statusList.isEmpty()) {
            resultList = comTxnTaskInboxRepository.findTasksByUserStatusRuleDateOrgWithStatus(
                    currentUser, statusList, ruleCode, filterDate, orgCode, slaOverdueDays);
        } else {
            resultList = comTxnTaskInboxRepository.findTasksByUserStatusRuleDateOrgWithoutStatus(
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
            ResponseData<List<UserDTO>> userList = usersFeignClient.getListUserInfo(new ArrayList<>(assigneeUsernames));
            if (userList != null && userList.getData() != null) {
                for (UserDTO user : userList.getData()) {
                    userMap.put(user.getUsername(), user); // Đảm bảo UserDTO có getUsername()
                }
            }
        }

        // Khi map từng task
        List<TaskListDto> dtoList = resultList.stream().map(map -> {
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

        return dtoList;
    }

    private String formatTimestamp(Object timestamp) {
        return timestamp instanceof Timestamp ? FORMATTER.format(((Timestamp) timestamp).toLocalDateTime()) : null;
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
                .collect(Collectors.toList());

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
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.output("cur_result", Types.REF_CURSOR));

        StoredProcedureResponse response = storedProcedureService.execute("SP_GET_TASKS_ASSIGNED_TO_USER", params);

        if (response == null || !response.isSuccess()) {
            log.error("Failed to execute SP_GET_TASKS_ASSIGNED_TO_USER procedure");
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
        List<Map<String, Object>> curAssigned = new ArrayList<>();
        List<Map<String, Object>> curUnassigned = new ArrayList<>();

        try {
            Map<String, Object> outputParams = response.getOutputParameters();
            if (outputParams == null) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            curAssigned = convertOutputCursorToListOfMap(outputParams.get("cur_assigned"));
            curUnassigned = convertOutputCursorToListOfMap(outputParams.get("cur_unassigned"));
        } catch (Exception e) {
            log.error("Error processing output cursor from StoredProcedureResponse: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return processTask(curAssigned, curUnassigned, pageable);
    }

    private List<Map<String, Object>> convertOutputCursorToListOfMap(Object cursor) {
        if (cursor instanceof List<?>) {
            return ((List<?>) cursor).stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<String, Object>) item)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Page<TxnTaskInboxSummaryDto> processTask(List<Map<String, Object>> curAssigned,
            List<Map<String, Object>> curUnassigned, Pageable pageable) {
        List<TxnTaskInboxSummaryDto> summaryList = new ArrayList<>();
        curAssigned.forEach(record -> summaryList.add(convertToDto(record, true)));
        curUnassigned.forEach(record -> summaryList.add(convertToDto(record, false)));

        return new PageImpl<>(summaryList, pageable, summaryList.size());
    }

    private TxnTaskInboxSummaryDto convertToDto(Map<String, Object> record, boolean isAssigned) {
        TxnTaskInboxSummaryDto dto = new TxnTaskInboxSummaryDto();
        String assigneeUsername = (String) record.get("ASSIGNEE");

        if (isAssigned && assigneeUsername != null && !assigneeUsername.isEmpty()) {
            ResponseData<List<UserDTO>> userList = usersFeignClient.getListUserInfo(List.of(assigneeUsername));
            if (userList.getData() != null && !userList.getData().isEmpty()) {
                UserDTO user = userList.getData().get(0);
                dto.setAssignee(user.getFirstName() + " " + user.getLastName());
            } else {
                dto.setAssignee(assigneeUsername);
            }
        } else {
            dto.setAssignee(assigneeUsername);
        }

        int notStarted = convertObjectToInt(record.get("NOT_STARTED"));
        int inProgress = convertObjectToInt(record.get("IN_PROGRESS"));
        int completed = convertObjectToInt(record.get("COMPLETED"));
        String ruleCode = (String) record.get("RULE_CODE");

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
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public StoredProcedureResponse callProcedureGetTaskSummary(String filterDate, String orgCode) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input("p_filter_date", filterDate),
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.output("cur_assigned", Types.REF_CURSOR),
                StoredProcedureParameter.output("cur_unassigned", Types.REF_CURSOR));
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
                    curAssigned = convertOutputCursorToListOfMap(outputParams.get("cur_assigned"));
                    curUnassigned = convertOutputCursorToListOfMap(outputParams.get("cur_unassigned"));
                }
            } catch (Exception e) {
                log.error("Error processing output cursor from StoredProcedureResponse: {}", e.getMessage(), e);
            }
        }

        return processTask(curAssigned, curUnassigned, pageable);
    }

    private Integer getSlaOverdueDays() {
        try {
            String slaOverdueValue = comCfgParameterService.getParameterByCode("SLA_OVERDUE_DAYS").getParamValue();
            return slaOverdueValue != null ? Integer.parseInt(slaOverdueValue) : null;
        } catch (NumberFormatException ex) {
            log.error("SLA_OVERDUE_DAYS is not a valid number: {}", ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            log.error("Error retrieving SLA_OVERDUE_DAYS: {}", ex.getMessage(), ex);
            return null;
        }
    }

    public StoredProcedureResponse callProcedureGetTaskOverdue(String orgCode, Integer slaOverdueDays) {
        List<StoredProcedureParameter> params = Arrays.asList(
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.input("p_sla_overdue_days", slaOverdueDays),
                StoredProcedureParameter.output("cur_assigned", Types.REF_CURSOR),
                StoredProcedureParameter.output("cur_unassigned", Types.REF_CURSOR));
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
                StoredProcedureParameter.input("p_filter_date", filterDate),
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.output("not_started", Types.INTEGER),
                StoredProcedureParameter.output("in_progress", Types.INTEGER),
                StoredProcedureParameter.output("completed", Types.INTEGER),
                StoredProcedureParameter.output("overdue", Types.INTEGER));
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
                StoredProcedureParameter.input("p_filter_date", filterDate),
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.output("not_started", Types.INTEGER),
                StoredProcedureParameter.output("in_progress", Types.INTEGER),
                StoredProcedureParameter.output("completed", Types.INTEGER),
                StoredProcedureParameter.output("overdue", Types.INTEGER));
        return storedProcedureService.execute("SP_GET_TASK_PERCENTAGE", params);
    }

    private TxnTaskStatusDto parseTaskStatusResponse(StoredProcedureResponse response) {
        TxnTaskStatusDto statusDto = new TxnTaskStatusDto();
        if (response != null) {
            try {
                Map<String, Object> outputParams = response.getOutputParameters();
                if (outputParams != null) {
                    statusDto.setNotStarted((Integer) outputParams.getOrDefault("not_started", 0));
                    statusDto.setInProgress((Integer) outputParams.getOrDefault("in_progress", 0));
                    statusDto.setCompleted((Integer) outputParams.getOrDefault("completed", 0));
                    statusDto.setOverdue((Integer) outputParams.getOrDefault("overdue", 0));
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
        Page<ActHiTaskIntResponse> responses = comTxnTaskInboxRepository.searchTaskListAdvance(
                request.getProcessInstanceCode(), request.getProcInstId_(),
                request.getTaskId(), request.getCreatedDateStart(), request.getCreatedDateEnd(),
                request.getTaskUpdateTimeStart(), request.getTaskUpdateTimeEnd(), pageRequest);

        return responses.map(item -> {
            ComTxnTaskInboxResponse response = comTxnTaskInboxMapper.toResponse(item);
            response.setCreatedDate(DateUtils.formatDateTime(item.getCreatedDate()));
            response.setTaskUpdateTime(DateUtils.formatDateTime(item.getTaskUpdateTime()));
            return response;
        });
    }

    @Override
    public void changeIsSuspend(Integer isSuspended, String taskId) {
        ComTxnTaskInbox entity = comTxnTaskInboxRepository.findByTaskIdAndIsDelete(taskId, 0)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, taskId));
        entity.setIsSuspend(isSuspended);
        comTxnTaskInboxRepository.save(entity);
    }
}
