package ngvgroup.com.bpm.client.dto.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDto {
    private Date txnDate;
    private String createdBy;
    private String processTypeName;
    private String txnContent;
}
