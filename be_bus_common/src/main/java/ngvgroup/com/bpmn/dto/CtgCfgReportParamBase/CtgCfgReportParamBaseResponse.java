package ngvgroup.com.bpmn.dto.CtgCfgReportParamBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgReportParamBaseResponse extends CtgCfgReportParamBaseDto {
    private Long id;

    public CtgCfgReportParamBaseResponse(Long id, String paramBaseCode, String paramBaseName, String orgCode,
                                         String paramBaseType, String description, String orgName){
        super(paramBaseCode, paramBaseName, orgCode, paramBaseType, description,orgName);
        this.id = id;
    }
}
