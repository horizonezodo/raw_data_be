package ngvgroup.com.bpm.core.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessData {
    private String processInstanceCode;
    private String referenceCode;
    private String orgCode;
    private String customerCode;
    private String customerName;
    private String txnContent;
    private InterpretiveStructureDto interpretiveStructureDto;
}
