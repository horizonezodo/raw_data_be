package ngvgroup.com.rpt.features.transactionreport.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Entity
@Table(name = "RPT_TXN_STAT_TEMPLATE_KPI")
@Getter
@Setter
public class RptTxnStatTemplateKpi extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
    private String templateCode;

    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "AGGREGATION_RUN_NO", nullable = false)
    private Integer aggregationRunNo;

    @Column(name = "REV_NO", nullable = false)
    private Integer revNo;

    @Column(name = "KPI_CODE", nullable = false, length = 32)
    private String kpiCode;

    @Column(name = "KPI_NAME", nullable = false, length = 512)
    private String kpiName;

    @Column(name = "REPORT_DATA_DATE", nullable = false)
    private Date reportDataDate;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "KPI_VALUE", length = 4000)
    private String kpiValue;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}

