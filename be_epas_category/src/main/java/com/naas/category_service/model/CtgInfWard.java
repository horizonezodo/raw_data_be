package com.naas.category_service.model;



import com.naas.category_service.constant.StatusConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CTG_INF_WARD")
@Getter
@Setter
@NoArgsConstructor
public class CtgInfWard extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name="WARD_CODE",nullable = false,length = 128)
    private String wardCode;

    @Column(name="WARD_NAME",nullable = false)
    private String wardName;

    @Column(name="DISTRICT_CODE",length = 128,nullable = false)
    private String districtCode;

    @Column(name="PROVINCE_CODE",nullable = false,length = 128)
    private String provinceCode;

    public CtgInfWard(String wardCode, String wardName, String provinceCode,String description) {
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.provinceCode = provinceCode;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.setIsActive(true);
    }
}
