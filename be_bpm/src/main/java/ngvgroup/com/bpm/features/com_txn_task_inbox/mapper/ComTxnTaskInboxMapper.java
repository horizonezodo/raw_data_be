package ngvgroup.com.bpm.features.com_txn_task_inbox.mapper;

import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.ActHiTaskIntResponse;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.ComTxnTaskInboxResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComTxnTaskInboxMapper {
    ComTxnTaskInboxResponse toResponse(ActHiTaskIntResponse response);
}
