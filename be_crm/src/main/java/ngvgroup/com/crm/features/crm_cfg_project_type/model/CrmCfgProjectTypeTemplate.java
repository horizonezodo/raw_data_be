package ngvgroup.com.crm.features.crm_cfg_project_type.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CRM_CFG_PROJECT_TYPE_TEMPLATE")
public class CrmCfgProjectTypeTemplate extends BaseEntity {

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 50)
    private String templateCode;

    @Column(name = "ORG_CODE", length = 50,nullable = false)
    private String orgCode;

    @Column(name = "PROJECT_TYPE_CODE", nullable = false, length = 128)
    private String projectTypeCode;

    @Column(name = "IS_REQUIRED")
    private Integer isRequired;
}