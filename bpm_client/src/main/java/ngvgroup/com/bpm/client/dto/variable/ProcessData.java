package ngvgroup.com.bpm.client.dto.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessData {
    private String processInstanceCode;
    private String referenceCode;
    private String orgCode;
    private String customerCode;
    private String customerName;
    private String txnContent;
    private InterpretiveStructureDto interpretiveStructureDto;
}
