package ngvgroup.com.bpmn.mapper.ruleMapper;

import ngvgroup.com.bpmn.dto.TxnTaskInbox.ActHiTaskIntResponse;
import ngvgroup.com.bpmn.dto.TxnTaskInbox.ComTxnTaskInboxResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComTxnTaskInboxMapper {
    ComTxnTaskInboxResponse toResponse(ActHiTaskIntResponse response);
}
