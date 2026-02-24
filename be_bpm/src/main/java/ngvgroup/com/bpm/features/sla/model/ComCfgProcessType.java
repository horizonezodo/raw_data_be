package ngvgroup.com.bpm.features.sla.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_CFG_PROCESS_TYPE")
public class ComCfgProcessType extends BaseEntity {

    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", length = 256)
    private String processTypeName;

    @Column(name = "PROCESS_GROUP_CODE", length = 64, nullable = false)
    private String processGroupCode;

    @Column(name = "PROCESS_GROUP_NAME", length = 128)
    private String processGroupName;

    @Column(name = "MODULE_CODE", length = 64)
    private String moduleCode;

    @Column(name = "IS_ACCOUNTING")
    private Integer isAccounting;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
