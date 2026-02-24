package ngvgroup.com.bpmn.dto.Process;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchProcessDTO {
    private String processInstanceCode;

    private List<String> processTypeCodeList;
    private String customerCode;
    private String orgCode;
    private String fromDate;
    private String toDate;
    private List<String> businessStatusList;
    private String slaProcessColor;
    private String slaResult;
    private String filter;

    private List<String> labels;
    private PageableDTO pageable;
}
