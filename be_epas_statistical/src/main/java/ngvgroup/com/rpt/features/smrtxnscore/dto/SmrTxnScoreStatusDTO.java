package ngvgroup.com.rpt.features.smrtxnscore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmrTxnScoreStatusDTO {
    private String actionName;
    private String statusCode;
    private String statusName;
    private String transitionComment;
    private String txnUserId;
    private String txnUserName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    private Timestamp transitionAt;

    public SmrTxnScoreStatusDTO(String statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }
}
