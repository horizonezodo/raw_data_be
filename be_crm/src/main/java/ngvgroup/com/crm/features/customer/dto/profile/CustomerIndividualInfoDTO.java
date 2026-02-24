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
public class CustomerIndividualInfoDTO {
    // --- Bảng INDV ---
    @AuditField("genderCode")
    private String genderCode;

    @AuditField("dateOfBirth")
    private Date dateOfBirth;

    @AuditField("placeOfBirth")
    private String placeOfBirth;

    @AuditField("ethnicityCode")
    private String ethnicityCode;

    @AuditField("maritalStatus")
    private String maritalStatus;

    @AuditField("professionTypeCode")
    private String professionTypeCode;

    @AuditField("eduLevelCode")
    private String eduLevelCode;

    @AuditField("eduBackgroundCode")
    private String eduBackgroundCode;

    // --- Bảng DOC ---
    @AuditField("identificationType")
    private String identificationType;

    @AuditField("identificationId")
    private String identificationId;

    @AuditField("identificationIdOld")
    private String identificationIdOld;

    @AuditField("issueDate")
    private Date issueDate;

    @AuditField("expiryDate")
    private Date expiryDate;

    @AuditField("issuePlace")
    private String issuePlace;
}
