package com.naas.admin_service.features.category.model;



import com.naas.admin_service.core.contants.Constant;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="COM_INF_WARD")
@Getter
@Setter
@NoArgsConstructor
public class CtgInfWard extends BaseEntity{
    @Column(name="WARD_CODE",nullable = false,length = 128)
    private String wardCode;

    @Column(name="WARD_NAME",nullable = false)
    private String wardName;

    @Column(name="DISTRICT_CODE",length = 128,nullable = false)
    private String districtCode;

    @Column(name="PROVINCE_CODE",nullable = false,length = 128)
    private String provinceCode;

    @Column(
      name = "RECORD_STATUS",
      length = 64
   )
   private String recordStatus = "approval";

   @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public CtgInfWard(String wardCode, String wardName, String provinceCode,String description) {
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.provinceCode = provinceCode;
        this.setDescription(description);
        this.setRecordStatus(Constant.APPROVAL);
    }
}
