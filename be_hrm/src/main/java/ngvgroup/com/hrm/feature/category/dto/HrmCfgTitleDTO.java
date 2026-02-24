package ngvgroup.com.hrm.feature.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class HrmCfgTitleDTO {

    @NotNull
    private Long id;

    private Integer isActive;

    @Size(max = 512)
    private String description;

    @NotBlank
    @Size(max = 64)
    private String orgCode;

    @NotBlank
    @Size(max = 32)
    private String titleCode;

    @NotBlank
    @Size(max = 128)
    private String titleName;

    private String orgName;

    public HrmCfgTitleDTO(Long id, String titleCode, String titleName, String orgCode, String orgName, Integer isActive, String description) {
        this.id = id;
        this.titleCode = titleCode;
        this.titleName = titleName;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.isActive = isActive;
        this.description = description;
    }
}
