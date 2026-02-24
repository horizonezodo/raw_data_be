package com.naas.admin_service.features.category.model;

import com.naas.admin_service.core.contants.Constant;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="COM_INF_PROVINCE")
@NoArgsConstructor
@Setter
@Getter
public class CtgInfProvince extends BaseEntity{
    @Column(name="PROVINCE_CODE",length = 128,nullable = false)
    private String provinceCode;

    @Column(name="PROVINCE_NAME",length = 128,nullable = false)
    private String provinceName;

    @Column(name="TAX_CODE",length = 64)
    private String taxCode;

    @Column(
      name = "RECORD_STATUS",
      length = 64
   )
   private String recordStatus = "approval";

   @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;


    public CtgInfProvince(String provinceCode, String provinceName, String taxCode,String description) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.taxCode = taxCode;
        this.setDescription(description);
        this.setRecordStatus(Constant.APPROVAL);
    }

}
