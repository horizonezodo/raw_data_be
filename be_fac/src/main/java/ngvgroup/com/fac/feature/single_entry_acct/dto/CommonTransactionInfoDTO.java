package ngvgroup.com.fac.feature.single_entry_acct.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonTransactionInfoDTO {
    private String processInstanceCode;
    private String orgCode;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate txnDate;
}
