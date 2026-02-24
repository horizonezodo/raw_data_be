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
@Table(name = "COM_CFG_TXN_CONTENT_DTL")
public class ComCfgTxnContentDtl extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "SORT_NUMBER", nullable = false, length = 128)
    private Integer sortNumber;

    @Column(name = "CONTENT_CODE", nullable = false, length = 128)
    private String contentCode;

    @Column(name = "CONTENT_DTL_CODE",  nullable = false, length = 256)
    private String contentDtlCode;

    @Column(name = "CONTENT_VALUE_TYPE", nullable = false, length = 64)
    private String contentValueType;

    @Column(name = "CONTENT_VALUE", nullable = false, length = 256)
    private String contentValue;

    @Column(name = "FORMAT_MASK", length = 128)
    private String formatMask;

    @Column(name = "LENGTH", length = 22)
    private Integer length;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}

