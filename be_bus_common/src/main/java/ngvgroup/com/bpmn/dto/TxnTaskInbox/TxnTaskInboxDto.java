package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import java.sql.Timestamp;

public interface TxnTaskInboxDto {
    String getId();
    String getTaskDefKey();
    Timestamp getCreateTime();
    Timestamp getLastUpdated();
}
