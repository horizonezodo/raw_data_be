package ngvgroup.com.hrm.feature.employee.dto;

import lombok.Data;

import java.util.List;

@Data
public class HrmProfileDto {
    private HrmBasicInfoDto basicInfo;
    private List<HrmPositionInfoDto> positionInfos;
    private List<HrmFamilyInfoDto> familyInfos;
    private List<HrmEduInfoDto> eduInfos;
    private List<HrmAuthInfoDto> authInfos;
}
