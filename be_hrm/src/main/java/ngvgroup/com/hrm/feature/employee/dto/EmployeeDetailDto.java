package ngvgroup.com.hrm.feature.employee.dto;

import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.variable.TransactionHistoryDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EmployeeDetailDto {
    private HrmProfileDto profile;
    private List<ProcessFileDto> files;
    private List<TransactionHistoryDto> histories;
}
