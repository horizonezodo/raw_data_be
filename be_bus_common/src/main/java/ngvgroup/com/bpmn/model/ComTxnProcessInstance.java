package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_TXN_PROCESS_INSTANCE")
public class ComTxnProcessInstance extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;

    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;

    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;

    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", length = 256)
    private String processTypeName;

    @Column(name = "TXN_AMT")
    private Double txnAmt;

    @Column(name = "TXN_CONTENT", length = 256)
    private String txnContent;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME", length = 256)
    private String customerName;

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "SLA_PROCESS_CODE", length = 128)
    private String slaProcessCode;

    @Column(name = "SLA_TYPE", length = 32)
    private String slaType;

    @Column(name = "SLA_MAX_DURATION")
    private Double slaMaxDuration;

    @Column(name = "SLA_WARNING_TYPE", length = 64)
    private String slaWarningType;

    @Column(name = "SLA_WARNING_DURATION")
    private Double slaWarningDuration;

    @Column(name = "SLA_WARNING_PERCENT")
    private Double slaWarningPercent;

    @Column(name = "SLA_STATUS", length = 64)
    private String slaStatus;

    @Column(name = "SLA_EVALUATED_AT")
    private Timestamp slaEvaluatedAt;
}



