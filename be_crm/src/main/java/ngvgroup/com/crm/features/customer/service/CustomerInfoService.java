package ngvgroup.com.crm.features.customer.service;

import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerInfoDto;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchResultDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchRequestDto;

public interface CustomerInfoService {
    Page<CustomerSearchResultDTO> searchCustomers(CustomerSearchRequestDto request, Pageable pageable);

    CustomerRelationDetailDTO getCustomerRelationDetail(String customerCode);

    Page<CustomerRelationDetailDTO> searchCustomerRelations(String keyword, Pageable pageable);

    CustomerInfoDto getCustomerDetail(String customerCode);

    byte[] generateTemplateFile(CustomerProfileDTO dto, TemplateResDto template);

    TemplateResDto getTemplateFileDetail();
}
