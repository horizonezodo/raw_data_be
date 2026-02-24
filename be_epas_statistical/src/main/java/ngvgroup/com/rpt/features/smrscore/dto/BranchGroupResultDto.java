package ngvgroup.com.rpt.features.smrscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGroupResultDto {
    private BranchGroupInfo info;
    private List<BranchGroupKpiResultDto> kpiResultDtoList;

    public String getScoreInstantCode() {
        return info.getScoreInstantCode();
    }

    public String getCiId() {
        return info.getCiId();
    }

    public String getCiBrId() {
        return info.getCiBrId();
    }

    public String getStatScoreGroupCode() {
        return info.getStatScoreGroupCode();
    }

}
