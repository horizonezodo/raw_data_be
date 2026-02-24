package ngvgroup.com.bpmn.service.impl;

import com.ngvgroup.bpm.core.service.BaseService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import ngvgroup.com.bpmn.dto.Process.*;
import ngvgroup.com.bpmn.repository.ComTxnProcessInstanceRepository;
import ngvgroup.com.bpmn.service.ComTxnProcessInstanceService;
import ngvgroup.com.bpmn.utils.PageUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComTxnProcessInstanceServiceImpl extends BaseService implements ComTxnProcessInstanceService {

    private final ComTxnProcessInstanceRepository comTxnProcessInstanceRepository;

    private final ExcelService excelService;




    @Override
    public Page<ComTxnProcessDto> getProcess(PageableDTO pageableDTO){
        Pageable pageable=PageUtils.toPageable(pageableDTO);
        return comTxnProcessInstanceRepository.getProcess(pageable,getCurrentUserId() );
    }

    @Override
    public List<String> getStates(){
        return comTxnProcessInstanceRepository.getStates();
    }

    @Override
    public Page<ComTxnProcessDto> findProcess(String keyword,Pageable pageable){

        return comTxnProcessInstanceRepository.findProcess(keyword,pageable,getCurrentUserId());
    }

    @Override
    public Page<ComTxnProcessDto> searchAdvance(SearchProcessDTO searchProcessDTO,Pageable pageable){

        return comTxnProcessInstanceRepository.searchAdvance(searchProcessDTO.getCustomerCode(),
                searchProcessDTO.getProcessInstanceCode(),
                searchProcessDTO.getOrgCode(),
                searchProcessDTO.getBusinessStatusList(),
                searchProcessDTO.getSlaResult(),
                searchProcessDTO.getProcessTypeCodeList(),
                searchProcessDTO.getFromDate(),
                searchProcessDTO.getToDate(),
                searchProcessDTO.getSlaProcessColor(),
                searchProcessDTO.getBusinessStatusList().size(),
                searchProcessDTO.getProcessTypeCodeList().size(),
                getCurrentUserId(),
                pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(SearchProcessDTO searchProcessDTO, String fileName){

        List<ComTxnProcessDto> comTxnProcessDtos=comTxnProcessInstanceRepository.exportToExcel(
                searchProcessDTO.getCustomerCode(),
                searchProcessDTO.getProcessInstanceCode(),
                searchProcessDTO.getOrgCode(),
                searchProcessDTO.getBusinessStatusList(),
                searchProcessDTO.getSlaResult(),
                searchProcessDTO.getProcessTypeCodeList(),
                searchProcessDTO.getFromDate(),
                searchProcessDTO.getToDate(),
                searchProcessDTO.getSlaProcessColor(),
                searchProcessDTO.getBusinessStatusList().size(),
                searchProcessDTO.getProcessTypeCodeList().size(),
                getCurrentUserId()
        );

        List<ComTxnProcessExcel> exportDtos = comTxnProcessDtos.stream()
                .map(dto -> {
                    ComTxnProcessExcel exportDto = new ComTxnProcessExcel();
                   exportDto.setProcessInstanceCode(dto.getProcessInstanceCode());
                   exportDto.setProcessTypeName(dto.getProcessTypeName());
                   exportDto.setCreatedBy(dto.getCreatedBy());
                   exportDto.setSlaMaxDuration(dto.getSlaMaxDuration());
                   exportDto.setCreatedDate(dto.getCreatedDate());
                   exportDto.setSlaProcessDeadline(dto.getSlaProcessDeadline());
                   exportDto.setModifiedDate(dto.getModifiedDate());
                   exportDto.setDescription(dto.getDescription());
                   exportDto.setBusinessStatus(dto.getBusinessStatus());
                   exportDto.setSlaResult(dto.getSlaResult());
                   exportDto.setSlaWarningType(dto.getSlaWarningType());
                   exportDto.setSlaWarningDuration(dto.getSlaWarningDuration());
                   exportDto.setSlaWarningPercent(dto.getSlaWarningPercent());

                    return exportDto;
                })
                .collect(Collectors.toList());
        exportDtos.forEach(item -> {
            switch (item.getBusinessStatus()) {
                case "COMPLETE":
                    item.setBusinessStatus("Hoàn thành");
                    break;
                case "ACTIVE":
                    item.setBusinessStatus("Đang thực hiện");
                    break;
                case "CANCEL":
                    item.setBusinessStatus("Hủy bỏ");
                    break;
            }
            String resultVi = convertSlaResultToVietnamese(item);
            item.setSlaResult(resultVi);
            item.setSlaWarningType(null);
            item.setSlaWarningDuration(null);
            item.setSlaWarningPercent(null);
        });

        return excelService.exportToExcel(exportDtos, searchProcessDTO.getLabels(), ComTxnProcessExcel.class, fileName);
    }

    private String convertSlaResultToVietnamese(ComTxnProcessExcel item) {
        String status = item.getBusinessStatus();


        if ("BREACHED".equals(item.getSlaResult()) &&
                ("Hoàn thành".equals(status) || "Hủy bỏ".equals(status))) {
            return "Quá hạn";
        }

        if ("ACHIEVED".equals(item.getSlaResult()) &&
                ("Hoàn thành".equals(status) || "Hủy bỏ".equals(status))) {
            return "Trong hạn";
        }

        // Trường hợp không có SLA_RESULT, cần tính toán
        if (item.getSlaResult() == null &&
                item.getSlaWarningType() != null &&
                item.getCreatedDate() != null) {

            long minutesSinceCreated = ChronoUnit.MINUTES.between(
                    item.getCreatedDate().toInstant(),
                    Instant.now()
            );

            if ("FIXED".equals(item.getSlaWarningType())) {
                if (minutesSinceCreated < item.getSlaWarningDuration()) {
                    return "Trong hạn";
                } else if (minutesSinceCreated <= item.getSlaMaxDuration()) {
                    return "Sắp quá hạn";
                } else {
                    return "Quá hạn";
                }
            }

            if ("PERCENT".equals(item.getSlaWarningType())) {
                long threshold = Math.round(item.getSlaMaxDuration() * item.getSlaWarningPercent());

                if (minutesSinceCreated < threshold) {
                    return "Trong hạn";
                } else if (minutesSinceCreated <= item.getSlaMaxDuration()) {
                    return "Sắp quá hạn";
                } else {
                    return "Quá hạn";
                }
            }
        }

        return "Không xác định";
    }





    @Override
    public List<ProcessTypeDto> getProcessTypes(){
        return comTxnProcessInstanceRepository.getProcessTypes();
    }





}
