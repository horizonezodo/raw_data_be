package ngvgroup.com.bpm.features.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTransactionHistoryDto {
    private Date txnDate;
    private String createdBy;
    private String processTypeName;
    private String txnContent;
}
