package ngvgroup.com.rpt.features.ctgcfgresource.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "COM_CFG_RESOURCE_MAPPING")
public class CtgCfgResourceMapping extends BaseEntity {
    @Column(name = "RESOURCE_TYPE_CODE", length = 64, nullable = false)
    private String resourceTypeCode;
    @Column(name = "USER_ID", length = 64)
    private String userId;
    @Column(name = "GROUP_ID", length = 64)
    private String groupId;
    @Column(name = "RESOURCE_CODE", length = 64, nullable = false)
    private String resourceCode;
    @Column(name = "RESOURCE_NAME", length = 64, nullable = false)
    private String resourceName;
    @Column(name = "RESOURCE_DESC", length = 64)
    private String resourceDesc;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
