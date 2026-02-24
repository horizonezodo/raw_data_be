package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.client.annotation.AuditField;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerGeneralInfoDTO {
    
    @AuditField("txnDate")
    private Date txnDate;

    @AuditField("processInstanceCode")
    private String processInstanceCode;

    @AuditField("customerCode")
    private String customerCode;

    @AuditField("customerName")
    private String customerName;

    @AuditField("customerType")
    private String customerType;

    @AuditField("orgCode")
    private String orgCode;

    @AuditField("areaCode")
    private String areaCode;

    @AuditField("mobileNumber")
    private String mobileNumber;

    @AuditField("phoneNumber")
    private String phoneNumber;

    @AuditField("email")
    private String email;

    @AuditField("taxCode")
    private String taxCode;

    @AuditField("fax")
    private String fax;

    @AuditField("economicTypeCode")
    private String economicTypeCode;

    @AuditField("industryCode")
    private String industryCode;

    @AuditField("isInsurance")
    private Integer isInsurance;
}
