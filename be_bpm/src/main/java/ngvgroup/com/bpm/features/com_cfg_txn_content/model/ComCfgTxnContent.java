package ngvgroup.com.bpm.features.com_cfg_txn_content.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_CFG_TXN_CONTENT")
public class ComCfgTxnContent extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CONTENT_CODE", nullable = false, length = 128)
    private String contentCode;

    @Column(name = "CONTENT_TEXT", nullable = false, length = 4000)
    private String contentText;

    @Column(name = "CONTENT_NAME", nullable = false, length = 256)
    private String contentName;

    @Column(name = "MODULE_CODE",  nullable = false, length = 128)
    private String moduleCode;

    @Column(name = "LENGTH",  nullable = false, length = 22)
    private Integer length;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
