package ngvgroup.com.hrm.feature.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmCfgPositionDTO {

    private Long id;

//    @NotBlank
    @Size(max = 64)
    private String orgCode;

    @NotBlank
    @Size(max = 32)
    private String positionCode;

    @NotBlank
    @Size(max = 128)
    private String positionName;

    @NotNull
    private Integer isManager;

    @NotBlank
    @Size(max = 64)
    private String orgLevelCode;

    private String orgLevelName;

    @Size(max = 32)
    private String titleCode;

    private Integer isActive;

    @Size(max = 512)
    private String description;

    private String titleName;

    public HrmCfgPositionDTO(Long id, String positionCode, String positionName, String titleName,
                             String orgLevelCode, Integer isManager) {
        this.id = id;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.titleName = titleName;
        this.isManager = isManager;
        this.orgLevelCode = orgLevelCode;
    }
}
