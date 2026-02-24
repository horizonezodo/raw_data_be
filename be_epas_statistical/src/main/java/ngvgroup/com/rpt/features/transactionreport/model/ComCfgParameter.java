package ngvgroup.com.rpt.features.transactionreport.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COM_CFG_PARAMETER")
public class ComCfgParameter extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "PARAM_CODE", length = 256, nullable = false)
    private String paramCode;
    @Column(name = "PARAM_NAME", length = 128, nullable = false)
    private String paramName;
    @Column(name = "VALUE_DESCRIPTION", length = 512, nullable = false)
    private String valueDescription;
    @Column(name = "PARAM_DEFAULT_VALUE", length = 256, nullable = false)
    private String paramDefaultValue;
    @Column(name = "PARAM_VALUE", length = 256, nullable = false)
    private String paramValue;
    @Column(name = "PARAM_TYPE", length = 64, nullable = false)
    private String paramType;
    @Column(name = "MODULE_CODE", length = 64)
    private String moduleCode;
    @Column(name = "MODULE_NAME", length = 128)
    private String moduleName;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
