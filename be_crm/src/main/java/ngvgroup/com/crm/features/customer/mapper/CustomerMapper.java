package ngvgroup.com.crm.features.customer.mapper;

import org.mapstruct.*;

import ngvgroup.com.crm.features.customer.dto.CustomerDetailDTO;
import ngvgroup.com.crm.features.customer.dto.profile.*;
import ngvgroup.com.crm.features.customer.model.txn.*;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    // 1. Bảng chung: Map tự động từ GeneralInfo + ExtensionInfo (cho description)
    @Mapping(target = "processTypeCode", constant = "CRM.200.01")
    @Mapping(target = "businessStatus", constant = "ACTIVE")
    CrmTxnCust toCrmTxnCust(CustomerGeneralInfoDTO gen, CustomerExtensionInfoDTO ext);

    // 2. Bảng Cá nhân: Map tự động từ IndividualInfo + ExtensionInfo (nghề nghiệp,
    // nơi làm việc...)
    CrmTxnCustIndv toIndv(CustomerIndividualInfoDTO indv, CustomerExtensionInfoDTO ext);

    // 3. Bảng Doanh nghiệp: Map tự động từ CorporateInfo + Tên KH từ GeneralInfo
    @Mapping(source = "gen.customerName", target = "corpName")
    CrmTxnCustCorp toCorp(CustomerCorporateInfoDTO corp, CustomerGeneralInfoDTO gen);

    // 4. Bảng Giấy tờ: Map tự động từ IndividualInfo
    CrmTxnCustDoc toDoc(CustomerIndividualInfoDTO indv);

    // 5. Bảng Phân khúc: Map tự động từ ExtensionInfo
    CrmTxnCustSeg toSeg(CustomerExtensionInfoDTO ext);

    // 6. Bảng Địa chỉ: Map tự động từ CustomerAddressDTO
    @Mapping(target = "countryCode", defaultValue = "VN")
    CrmTxnCustAddr toAddr(CustomerAddressDTO dto);

    // --- UPDATE METHODS (Sử dụng lại cấu hình mapping ở trên) ---

    @InheritConfiguration(name = "toCrmTxnCust")
    @Mapping(target = "processTypeCode", ignore = true)
    void updateCust(@MappingTarget CrmTxnCust entity, CustomerGeneralInfoDTO gen, CustomerExtensionInfoDTO ext);

    @InheritConfiguration(name = "toIndv")
    void updateIndv(@MappingTarget CrmTxnCustIndv entity, CustomerIndividualInfoDTO indv, CustomerExtensionInfoDTO ext);

    @InheritConfiguration(name = "toCorp")
    void updateCorp(@MappingTarget CrmTxnCustCorp entity, CustomerCorporateInfoDTO corp, CustomerGeneralInfoDTO gen);

    @InheritConfiguration(name = "toDoc")
    void updateDoc(@MappingTarget CrmTxnCustDoc entity, CustomerIndividualInfoDTO indv);

    @InheritConfiguration(name = "toSeg")
    void updateSeg(@MappingTarget CrmTxnCustSeg entity, CustomerExtensionInfoDTO ext);

    // --- MAPPING FROM CustomerDetailDTO TO PROFILE DTOs ---

    CustomerGeneralInfoDTO toGeneralInfo(CustomerDetailDTO detail);

    CustomerIndividualInfoDTO toIndividualInfo(CustomerDetailDTO detail);

    CustomerCorporateInfoDTO toCorporateInfo(CustomerDetailDTO detail);

    @Mapping(target = "workTimeValue", expression = "java(detail.getWorkTimeValue() != null ? Integer.valueOf(detail.getWorkTimeValue()) : null)")
    CustomerExtensionInfoDTO toExtensionInfo(CustomerDetailDTO detail);

    // --- CUSTOM LOGIC ---

    @AfterMapping
    default void calculateWorkTime(CustomerExtensionInfoDTO ext, @MappingTarget CrmTxnCustIndv entity) {
        if (ext != null && ext.getWorkTimeValue() != null && ext.getWorkTimeUnit() != null) {
            String unit = ext.getWorkTimeUnit().trim().toUpperCase();
            if (CrmVariableConstants.WORK_TIME_UNIT_WORK_YEARS.equals(unit)
                    || CrmVariableConstants.WORK_TIME_UNIT_YEARS.equals(unit)) {
                entity.setWorkYears(ext.getWorkTimeValue());
            } else if (CrmVariableConstants.WORK_TIME_UNIT_WORK_MONTHS.equals(unit)
                    || CrmVariableConstants.WORK_TIME_UNIT_MONTHS.equals(unit)) {
                entity.setWorkMonths(ext.getWorkTimeValue());
            }
        }
    }
}