package ngvgroup.com.bpm.features.sla.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;
import ngvgroup.com.bpm.core.contants.ExcelColumns;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComCfgSlaDto {
    private String orgName;
    private String processTypeName;
    private String slaType;
    private String slaWarningType;
    private Double slaMaxDuration;
    private Integer isActive;
    @ExcelColumn(value = ExcelColumns.ORG_CODE)
    private String orgCode;
    @ExcelColumn(value = ExcelColumns.PROCESS_TYPE_CODE)
    private String processTypeCode;
    @ExcelColumn(value = ExcelColumns.UNIT)
    private String unit;
    @ExcelColumn(value = ExcelColumns.PROCESS_DEFINE_CODE)
    private String processDefineCode;
    private String keyword;
    private Pageable pageable;
    private List<String> labels;

    ComCfgSlaProcessDto comCfgSlaProcessDto;
    ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto;

    public ComCfgSlaDto(ComCfgSlaProcessDto comCfgSlaProcessDto, ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto) {
        this.comCfgSlaProcessDto = comCfgSlaProcessDto;
        this.comCfgSlaProcessDtlDto = comCfgSlaProcessDtlDto;
    }

    List<ComCfgSlaTaskDto> comCfgSlaTaskDto;
    List<ComCfgSlaTaskDtlDto> comCfgSlaTaskDtlDto;

    public interface ComCfgSlaView {
        String getOrgCode();
        String getProcessTypeCode();
        String getOrgName();
        String getProcessTypeName();
        String getSlaType();
        String getSlaWarningType();
        Double getSlaMaxDuration();
        String getUnit();
        Integer getIsActive();
        String getProcessDefineCode();
    }

    public static ComCfgSlaDto fromView(ComCfgSlaView v) {
        return ComCfgSlaDto.builder()
                .orgCode(v.getOrgCode())
                .processTypeCode(v.getProcessTypeCode())
                .orgName(v.getOrgName())
                .processTypeName(v.getProcessTypeName())
                .slaType(v.getSlaType())
                .slaWarningType(v.getSlaWarningType())
                .slaMaxDuration(v.getSlaMaxDuration())
                .unit(v.getUnit())
                .isActive(v.getIsActive())
                .processDefineCode(v.getProcessDefineCode())
                .build();
    }

}
