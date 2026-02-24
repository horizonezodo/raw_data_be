package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustIndv extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "PLACE_OF_BIRTH", length = 128)
    private String placeOfBirth;

    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    @Column(name = "ETHNICITY_CODE", length = 32)
    private String ethnicityCode;

    @Column(name = "PROFESSION_TYPE_CODE", length = 64)
    private String professionTypeCode;

    @Column(name = "EDU_BACKGROUND_CODE", length = 32)
    private String eduBackgroundCode;

    @Column(name = "EDU_LEVEL_CODE", length = 32)
    private String eduLevelCode;

    @Column(name = "MARITAL_STATUS", length = 64)
    private String maritalStatus;

    @Column(name = "IS_POOR_HOUSEHOLD")
    private Integer isPoorHousehold;

    @Column(name = "POOR_HOUSEHOLD_BOOK_NO", length = 64)
    private String poorHouseholdBookNo;

    @Column(name = "PROFESSION", length = 256)
    private String profession;

    @Column(name = "WORKPLACE", length = 128)
    private String workplace;

    @Column(name = "WORK_ADDRESS", length = 256)
    private String workAddress;

    @Column(name = "JOB_TITLE", length = 64)
    private String jobTitle;

    @Column(name = "WORK_YEARS")
    private Integer workYears;

    @Column(name = "WORK_MONTHS")
    private Integer workMonths;

    @Column(name = "CONTRACT_TYPE", length = 64)
    private String contractType;
}