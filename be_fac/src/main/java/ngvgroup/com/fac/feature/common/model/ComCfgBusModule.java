package ngvgroup.com.fac.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "COM_CFG_BUS_MODULE")
@Immutable
public class ComCfgBusModule extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "MODULE_CODE", nullable = false, length = 64)
    private String moduleCode;

    @Column(name = "MODULE_NAME", nullable = false, length = 256)
    private String moduleName;

    @Column(name = "MODULE_TYPE_CODE", length = 64)
    private String moduleTypeCode;

    @Column(name = "MODULE_TYPE_NAME", length = 128)
    private String moduleTypeName;

    @Column(name = "SORT_NUMBER", length = 22)
    private Integer sortNumber;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
