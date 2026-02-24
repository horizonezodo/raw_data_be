package ngvgroup.com.crm.features.customer.service.impl;

import java.util.ArrayList;
import java.util.List;

import ngvgroup.com.bpm.client.dto.variable.TransactionHistoryDto;
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.common.service.CommonService;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.crm.core.constant.CrmErrorCode;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.crm.features.customer.dto.CustomerInfoDto;
import ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchRequestDto;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchResultDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerAddressDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerCorporateInfoDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerExtensionInfoDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerGeneralInfoDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerIndividualInfoDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerRelationDTO;
import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustAddr;
import ngvgroup.com.crm.features.customer.projection.CustomerProfileProjection;
import ngvgroup.com.crm.features.customer.repository.inf.CrmInfCustAddrRepository;
import ngvgroup.com.crm.features.customer.repository.inf.CrmInfCustRelnRepository;
import ngvgroup.com.crm.features.customer.repository.inf.CrmInfCustRepository;
import ngvgroup.com.crm.features.customer.service.CustomerInfoService;


@Service
@RequiredArgsConstructor
public class CustomerInfoServiceImpl implements CustomerInfoService {

    private final CrmInfCustRepository infCustRepo;
    private final CrmInfCustAddrRepository infCustAddrRepo;
    private final CrmInfCustRelnRepository infCustRelnRepo;
    private final CommonService commonService;
    private final BpmFeignClient bpmFeignClient;
    private final CustomerBpmService bpmService;
    private final CustomerTransactionService transactionService;


    @Override
    public Page<CustomerSearchResultDTO> searchCustomers(CustomerSearchRequestDto request, Pageable pageable) {
        if (request == null) {
            throw new BusinessException(CrmErrorCode.INVALID_REQUEST_DATA);
        }
        Page<CustomerSearchResultDTO> customerPage = infCustRepo.searchCustomers(request, pageable);
        long startIndex;
        try {
            startIndex = pageable.getOffset();
        } catch (Exception e) {
            startIndex = 0;
        }
        List<CustomerSearchResultDTO> content = customerPage.getContent();
        for (int i = 0; i < content.size(); i++) {
            content.get(i).setId(startIndex + i + 1);
        }
        return customerPage;
    }

    @Override
    public CustomerRelationDetailDTO getCustomerRelationDetail(String customerCode) {
        return infCustRepo.getCustomerForRelation(customerCode);
    }

    @Override
    public Page<CustomerRelationDetailDTO> searchCustomerRelations(String keyword, Pageable pageable) {
        return infCustRepo.searchCustomersForRelation(keyword, pageable);
    }

    private CustomerProfileDTO getProfileFromCustomerCode(String customerCode) {
        // 1. Gọi Repo lấy Projection từ bảng INF (Thông tin chung, cá nhân, doanh
        // nghiệp, phân khúc...)
        CustomerProfileProjection projection = infCustRepo.findInfProfileByCustomerCode(customerCode).orElse(null);

        if (projection == null) {
            return new CustomerProfileDTO();
        }

        // 2. Map General Info (Thông tin chung)
        CustomerGeneralInfoDTO generalInfo = CustomerGeneralInfoDTO.builder().txnDate(projection.getTxnDate()) // Lấy ngày sửa đổi cuối (ModifiedDate) từ query INF
                // .processInstanceCode(null) // INF không có ProcessInstanceCode đang chạy
                .customerCode(projection.getCustomerCode()).customerName(projection.getCustomerName()).customerType(projection.getCustomerType()).orgCode(projection.getOrgCode()).areaCode(projection.getAreaCode()).mobileNumber(projection.getMobileNumber()).phoneNumber(projection.getPhoneNumber()).email(projection.getEmail()).taxCode(projection.getTaxCode()).fax(projection.getFax()).economicTypeCode(projection.getEconomicTypeCode()).industryCode(projection.getIndustryCode()).isInsurance(projection.getIsInsurance()).build();

        // 3. Map Extension Info (Thông tin mở rộng)
        CustomerExtensionInfoDTO extensionInfo = CustomerExtensionInfoDTO.builder().poorHouseholdBookNo(projection.getPoorHouseholdBookNo()).isPoorHousehold(projection.getIsPoorHousehold()).segmentType(projection.getSegmentType()).segmentCode(projection.getSegmentCode()).segmentRankCode(projection.getSegmentRankCode()).profession(projection.getProfession()).jobTitle(projection.getJobTitle()).workTimeValue(projection.getWorkTimeValue() != null ? Integer.valueOf(projection.getWorkTimeValue()) : null).workTimeUnit(projection.getWorkTimeUnit()).contractType(projection.getContractType()).workplace(projection.getWorkplace()).workAddress(projection.getWorkAddress()).description(projection.getDescription()).build();

        // 4. Map Identity Info (Tách theo loại KH Cá nhân/Doanh nghiệp)
        CustomerIndividualInfoDTO individualInfo = null;
        CustomerCorporateInfoDTO corporateInfo = null;

        if (isIndividual(projection.getCustomerType())) {
            individualInfo = CustomerIndividualInfoDTO.builder().genderCode(projection.getGenderCode()).dateOfBirth(projection.getDateOfBirth()).placeOfBirth(projection.getPlaceOfBirth()).ethnicityCode(projection.getEthnicityCode()).maritalStatus(projection.getMaritalStatus()).professionTypeCode(projection.getProfessionTypeCode()).eduLevelCode(projection.getEduLevelCode()).eduBackgroundCode(projection.getEduBackgroundCode()).identificationType(projection.getIdentificationType()).identificationId(projection.getIdentificationId()).identificationIdOld(projection.getIdentificationIdOld()).issueDate(projection.getIssueDate()).expiryDate(projection.getExpiryDate()).issuePlace(projection.getIssuePlace()).build();
        } else {
            corporateInfo = CustomerCorporateInfoDTO.builder().corpShortName(projection.getCorpShortName()).businessLicenseNo(projection.getBusinessLicenseNo()).businessLicenseDate(projection.getBusinessLicenseDate()).issuedBy(projection.getIssuedBy()).establishedDate(projection.getEstablishedDate()).website(projection.getWebsite()).legalRepName(projection.getLegalRepName()).legalRepTitle(projection.getLegalRepTitle()).legalRepIdNo(projection.getLegalRepIdNo()).industryDetail(projection.getIndustryDetail()).build();
        }

        // 5. Map Addresses (Lấy từ bảng CRM_INF_CUST_ADDR)
        // Lưu ý: Cần đảm bảo CrmInfCustAddrRepository có method findAllByCustomerCode
        List<CustomerAddressDTO> addresses = new ArrayList<>();
        // Giả định repo trả về List<CrmInfCustAddr>, nếu repo hiện tại trả về Optional
        // (1 bản ghi) thì cần sửa repo hoặc xử lý list 1 phần tử
        List<CrmInfCustAddr> addrEntities = infCustAddrRepo.findAllByCustomerCode(customerCode);

        if (!CollectionUtils.isEmpty(addrEntities)) {
            addresses = addrEntities.stream().map(entity -> CustomerAddressDTO.builder().provinceCode(entity.getProvinceCode()).wardCode(entity.getWardCode()).address(entity.getAddress()).isPrimary(entity.getIsPrimary()).countryCode(entity.getCountryCode()).build()).toList();
        }

        List<CustomerRelationDTO> relations = infCustRelnRepo.findRelationsByCustomerCode(customerCode);

        // 7. Build và trả về DTO hoàn chỉnh
        return CustomerProfileDTO.builder().basicInfo(generalInfo).identityInfoPersonal(individualInfo).identityInfoCompany(corporateInfo).extendedInfo(extensionInfo).addressInfo(addresses).relations(relations).build();
    }

    private boolean isIndividual(String type) {
        return CrmVariableConstants.CUSTOMER_TYPE_INDIVIDUAL.equalsIgnoreCase(type);
    }

    @Override
    public CustomerInfoDto getCustomerDetail(String customerCode) {
        CustomerProfileDTO profile = getProfileFromCustomerCode(customerCode);
        List<ProcessFileDto> files = bpmFeignClient.getProcessFilesFromReferenceCode(customerCode, CrmVariableConstants.PROCESS_KEY_CUSTOMER_ADJUST).getData();
        List<TransactionHistoryDto> histories = bpmFeignClient.getTransactionHistory(customerCode, List.of(CrmVariableConstants.PROCESS_KEY_CUSTOMER_ADJUST, CrmVariableConstants.PROCESS_KEY_CUSTOMER_REGISTER)).getData();
        return new CustomerInfoDto(profile, files, histories);
    }

    public byte[] generateTemplateFile(CustomerProfileDTO dto, TemplateResDto template) {
        return bpmService.generateBusinessFile(dto, template);
    }

    public TemplateResDto getTemplateFileDetail() {
        return commonService.getTemplateByCode(CrmVariableConstants.CUSTOMER_REGISTER_TEMPLATE_FILE_CODE);
    }
}
