package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeepTrackActionsDto {
    private String statInstanceCode;     // Mã instance báo cáo
    private String transitionName;       // Tên hành động
    private String statusName;           // Trạng thái
    private String slaStatus;            // Cảnh báo SLA
    private String transitionComment;    // Nội dung
    private String txnUserId;            // Mã người tạo
    private String txnUserName;          // Tên người tạo
    private Timestamp transitionAt;      // Thời gian tạo
    private Timestamp slaDueAt;          // Thời điểm đến hạn
    private Timestamp slaActualAt;       // Thời điểm hoàn thành
}
