package ngvgroup.com.bpm.features.com_txn_task_inbox.service;

import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComTxnTaskInboxService {

        List<TaskListDto> getTaskList(String username, String ruleCode, List<String> listBusinessStatus,
                        String filterDate, String orgCode, Integer slaOverdueDays);

        Page<InboundTransactionsDto> inboundTransactions(InOutTransDto dto, Pageable pageable);

        Page<OutboundTransactionsDto> outboundTransactions(InOutTransDto dto, Pageable pageable);

        Page<TxnTaskInboxSummaryDto> getTaskSummary(String filterDate, String orgCode, Pageable pageable);

        Page<TxnTaskInboxSummaryDto> getTaskOverdue(String orgCode, Pageable pageable);

        TxnTaskStatusDto getTaskStatus(String filterDate, String orgCode);

        TxnTaskStatusDto getTaskPercentage(String filterDate, String orgCode);

        Page<AssignedTaskDto> getTasksAssignedToCurrentUser(String orgCode, Pageable pageable);

        Page<ComTxnTaskInboxResponse> searchTaskList(String filter, Pageable pageable);

        Page<ComTxnTaskInboxResponse> searchAdvanceTaskList(ComTxnTaskInboxSearch comTxnTaskInboxSearch,
                        Pageable pageRequest);

        void changeIsSuspend(Integer isSuspended, String taskId);
}
