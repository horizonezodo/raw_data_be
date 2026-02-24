package com.naas.category_service.model;


import com.naas.category_service.constant.StatusConstants;
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
@Table(name = "CTG_INF_INDUSTRY")
public class CtgInfIndustry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "INDUSTRY_CODE", nullable = false, length = 64)
    private String industryCode;

    @Column(name = "INDUSTRY_NAME", nullable = false, length = 256)
    private String industryName;

    public CtgInfIndustry(String orgCode, String industryCode, String industryName, Boolean isActive,String description) {
        this.orgCode = orgCode;
        this.industryCode = industryCode;
        this.industryName = industryName;
        this.isActive = isActive;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
    }
}
