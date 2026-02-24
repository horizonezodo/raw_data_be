package ngvgroup.com.crm.features.customer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.variable.TransactionHistoryDto;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;

@Data
@AllArgsConstructor
public class CustomerInfoDto {
    private CustomerProfileDTO profile;
    private List<ProcessFileDto> files;
    private List<TransactionHistoryDto> histories;
}
