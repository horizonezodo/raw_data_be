package com.naas.category_service.model;

import com.naas.category_service.constant.StatusConstants;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CTG_INF_PROVINCE")
@NoArgsConstructor
@Setter
@Getter
public class CtgInfProvince extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;


    @Column(name="PROVINCE_CODE",length = 128,nullable = false)
    private String provinceCode;

    @Column(name="PROVINCE_NAME",length = 128,nullable = false)
    private String provinceName;

    @Column(name="TAX_CODE",length = 64)
    private String taxCode;


    public CtgInfProvince(String provinceCode, String provinceName, String taxCode,String description) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.taxCode = taxCode;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.setIsActive(true);
    }

}
