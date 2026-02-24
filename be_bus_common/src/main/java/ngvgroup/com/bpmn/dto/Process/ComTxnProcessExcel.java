package ngvgroup.com.bpmn.dto.Process;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComTxnProcessExcel {

    private String processInstanceCode;

    private String processTypeName;
    private String createdBy;
    private Double slaMaxDuration;
    private Date createdDate;
    private Date slaProcessDeadline;
    private Date modifiedDate;
    private String description;
    private String businessStatus;
    private String slaResult;
    private String slaWarningType;
    private Double slaWarningDuration;
    private Double slaWarningPercent;



}
