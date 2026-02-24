package ngvgroup.com.bpmn.dto.ComCfgProcessType;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgProcessTypeDto {

    private String processTypeCode;
    private String processTypeName;

    private String processGroupCode;
}
