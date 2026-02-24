package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.client.annotation.AuditField;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerExtensionInfoDTO {
    // --- Hộ nghèo (INDV) ---  
    @AuditField("poorHouseholdBookNo")
    private String poorHouseholdBookNo;

    @AuditField("isPoorHousehold")
    private Integer isPoorHousehold;
    
    // --- Phân khúc (SEG) ---
    @AuditField("segmentType")
    private String segmentType;

    @AuditField("segmentCode")
    private String segmentCode;

    @AuditField("segmentRankCode")
    private String segmentRankCode;

    // --- Công việc (INDV) ---
    @AuditField("profession")
    private String profession; // Nhập tay

    @AuditField("jobTitle")
    private String jobTitle;

    @AuditField("workTimeValue")
    private Integer workTimeValue;

    @AuditField("workTimeUnit")
    private String workTimeUnit;

    @AuditField("contractType")
    private String contractType;

    @AuditField("workplace")
    private String workplace;

    @AuditField("workAddress")
    private String workAddress;

    // --- Ghi chú (CUST) ---   
    @AuditField("description")
    private String description; 
}
