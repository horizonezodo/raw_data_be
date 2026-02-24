package ngvgroup.com.loan.feature.interest_process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

import java.util.List;
@Data
@AllArgsConstructor
public class InterestDetailDTO {
    private InterestProfileDTO profile;
    private List<ProcessFileDto> files;
}
