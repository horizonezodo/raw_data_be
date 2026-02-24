package ngvgroup.com.crm.features.crm_cfg_project_type.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CRM_CFG_PROJECT_TYPE")
public class CrmCfgProjectType extends BaseEntity {

    @Column(name = "PROJECT_TYPE_CODE", nullable = false, length = 128)
    private String projectTypeCode;

    @Column(name = "PROJECT_TYPE_NAME", nullable = false, length = 256)
    private String projectTypeName;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

}