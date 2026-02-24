package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import java.sql.Timestamp;

public interface TxnTaskInboxDto {
    String getId();
    String getTaskDefKey();
    Timestamp getCreateTime();
    Timestamp getLastUpdated();
}
