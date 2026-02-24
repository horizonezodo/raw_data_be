package ngvgroup.com.bpm.features.com_txn_task_inbox.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.*;

import java.util.List;

import ngvgroup.com.bpm.features.com_txn_task_inbox.service.ComTxnTaskInboxService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/txn-task-inbox")
@AllArgsConstructor
public class ComTxnTaskInboxController {
    private final ComTxnTaskInboxService comTxnTaskInboxService;

    @Operation(summary = "Giao dịch đến")
    @PostMapping("inbound")
    @PreAuthorize("hasAnyRole('impl_process_registration_loan', 'impl_process_registration_customer')")
    public ResponseEntity<ResponseData<Page<InboundTransactionsDto>>> getListInbound(@RequestBody InOutTransDto dto,
                                                                                     @ParameterObject Pageable pageable) {
        Page<InboundTransactionsDto> page = comTxnTaskInboxService.inboundTransactions(dto, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(summary = "Giao dịch đi")
    @PostMapping("outbound")
    @PreAuthorize("hasAnyRole('impl_outcom_transaction','impl_process_registration_loan', 'impl_process_registration_customer')")
    public ResponseEntity<ResponseData<Page<OutboundTransactionsDto>>> getListOutBound(@RequestBody InOutTransDto dto,
            @ParameterObject Pageable pageable) {
        Page<OutboundTransactionsDto> page = comTxnTaskInboxService.outboundTransactions(dto, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(summary = "Lấy danh sách tổng hợp công việc theo filter ngày và chi nhánh")
    @GetMapping("task-summary")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<Page<TxnTaskInboxSummaryDto>>> getSummaryByFilter(
            @RequestParam String filterDate,
            @RequestParam String orgCode,
            Pageable pageable) {
        Page<TxnTaskInboxSummaryDto> page = comTxnTaskInboxService.getTaskSummary(filterDate, orgCode, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(summary = "Lấy danh sách tổng hợp công việc quá hạn theo filter ngày và chi nhánh")
    @GetMapping("task-overdue")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<Page<TxnTaskInboxSummaryDto>>> getOverdueByFilter(
            @RequestParam String orgCode,
            Pageable pageable) {
        Page<TxnTaskInboxSummaryDto> page = comTxnTaskInboxService.getTaskOverdue(orgCode, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(summary = "Lấy danh sách công việc được giao cho user hiện tại")
    @GetMapping("task-assigned")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<Page<AssignedTaskDto>>> getTasksAssignedToCurrentUser(
            @RequestParam String orgCode, Pageable pageable) {
        Page<AssignedTaskDto> page = comTxnTaskInboxService.getTasksAssignedToCurrentUser(orgCode, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(summary = "Lấy dữ liệu cho biểu đồ so sánh số lượng công việc theo trạng thái")
    @GetMapping("task-status")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<TxnTaskStatusDto>> getTaskStatus(@RequestParam String filterDate,
            @RequestParam String orgCode) {
        return ResponseData.okEntity(comTxnTaskInboxService.getTaskStatus(filterDate, orgCode));
    }

    @Operation(summary = "Lấy dữ liệu cho biểu đồ tỷ lệ phần trăm công việc")
    @GetMapping("task-percentage")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<TxnTaskStatusDto>> getTaskPercentage(@RequestParam String filterDate,
            @RequestParam String orgCode) {
        return ResponseData.okEntity(comTxnTaskInboxService.getTaskPercentage(filterDate, orgCode));
    }

    @Operation(summary = "Lấy danh sách công việc của user hiện tại với trạng thái ACTIVE hoặc COMPLETE")
    @GetMapping("task-list")
    @PreAuthorize("hasRole('category_dashboard_operate')")
    public ResponseEntity<ResponseData<List<TaskListDto>>> getTaskList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String ruleCode,
            @RequestParam(required = false) List<String> listBusinessStatus,
            @RequestParam(required = false) String filterDate,
            @RequestParam String orgCode,
            @RequestParam(required = false) Integer slaOverdueDays) {

        List<TaskListDto> list = comTxnTaskInboxService.getTaskList(username, ruleCode, listBusinessStatus, filterDate,
                orgCode, slaOverdueDays);
        return ResponseData.okEntity(list);
    }

    @Operation(summary = "Tìm kiếm danh sách tác vụ")
    @GetMapping("/monitor-tasks")
    @PreAuthorize("hasRole('admin_task_monitor')")
    public ResponseEntity<ResponseData<Page<ComTxnTaskInboxResponse>>> searchTaskList(@RequestParam String filter,
            @ParameterObject Pageable pageRequest) {
        return ResponseData.okEntity(comTxnTaskInboxService.searchTaskList(filter, pageRequest));
    }

    @Operation(summary = "Tìm kiếm nâng cao danh sách tác vụ")
    @PostMapping("/monitor-tasks/search")
    @PreAuthorize("hasRole('admin_task_monitor')")
    public ResponseEntity<ResponseData<Page<ComTxnTaskInboxResponse>>> searchTaskListAdvance(
            @RequestBody ComTxnTaskInboxSearch request, @ParameterObject Pageable pageRequest) {
        return ResponseData.okEntity(comTxnTaskInboxService.searchAdvanceTaskList(request, pageRequest));
    }

    @Operation(summary = "Thay đổi trạng thái isSuspend")
    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('admin_task_monitor')")
    public ResponseEntity<ResponseData<Void>> changeIsSuspendValue(@PathVariable String taskId,
            @RequestParam int isSuspend) {
        comTxnTaskInboxService.changeIsSuspend(isSuspend, taskId);
        return ResponseData.okEntity();
    }
}