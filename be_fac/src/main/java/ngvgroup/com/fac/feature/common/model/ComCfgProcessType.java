package ngvgroup.com.fac.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_CFG_PROCESS_TYPE")
@Immutable
public class ComCfgProcessType extends BaseEntity {

    @Column(name = "PROCESS_GROUP_CODE", length = 64)
    private String processGroupCode;

    @Column(name = "PROCESS_GROUP_NAME", length = 128)
    private String processGroupName;

    @Column(name = "PROCESS_TYPE_CODE", length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", length = 256)
    private String processTypeName;

    @Column(name = "PROCESS_TYPE", length = 128)
    private String processType;

    @Column(name = "MODULE_CODE", length = 64)
    private String moduleCode;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;

    @Column(name = "IS_ACCOUNTING", nullable = false, length = 1)
    private Integer isAccounting = 1;
}
