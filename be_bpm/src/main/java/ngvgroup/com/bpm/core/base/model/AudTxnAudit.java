package ngvgroup.com.bpm.core.base.model;

import java.util.Date;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "AUD_TXN_AUDIT")
@Data
public class AudTxnAudit extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;
    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;
    @Column(name = "FIELD_NAME", length = 128, nullable = false)
    private String fieldName;
    @Column(name = "FIELD_CODE", length = 128, nullable = false)
    private String fieldCode;
    @Column(name = "OLD_VALUE", length = 4000)
    private String oldValue;
    @Column(name = "NEW_VALUE", length = 4000)
    private String newValue;
}
