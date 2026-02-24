package com.naas.admin_service.features.category.model;

import com.naas.admin_service.core.contants.StatusConstants;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_INF_INDUSTRY")
public class CtgInfIndustry extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "INDUSTRY_CODE", nullable = false, length = 64)
    private String industryCode;

    @Column(name = "INDUSTRY_NAME", nullable = false, length = 256)
    private String industryName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public CtgInfIndustry(String orgCode, String industryCode, String industryName, Integer isActive,
            String description) {
        this.orgCode = orgCode;
        this.industryCode = industryCode;
        this.industryName = industryName;
        this.setIsActive(isActive);
        this.setDescription(description);
        this.setRecordStatus(StatusConstants.APPROVAL);
    }
}
