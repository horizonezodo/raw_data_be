package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_RESPONSE_DEFINE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatResponseDefine extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode; // Mã tổ chức

    @Column(name = "RESPONSE_CODE", nullable = false, length = 64)
    private String responseCode;  // Mã loại quy tắc

    @Column(name = "RESPONSE_NAME", nullable = false, length = 256)
    private String responseName; // Tên loại quy tắc

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
