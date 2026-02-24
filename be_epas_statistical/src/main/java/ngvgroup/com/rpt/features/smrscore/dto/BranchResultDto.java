package ngvgroup.com.rpt.features.smrscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResultDto {
    private BranchScoreCommonInfo info;
    private List<BranchGroupResultDto> groupResultDtoList;
    private String provCode;
}
