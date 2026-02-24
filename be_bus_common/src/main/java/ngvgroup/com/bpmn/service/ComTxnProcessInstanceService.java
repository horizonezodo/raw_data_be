package ngvgroup.com.bpmn.service;


import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import ngvgroup.com.bpmn.dto.Process.ComTxnProcessDto;
import ngvgroup.com.bpmn.dto.Process.ProcessDTO;
import ngvgroup.com.bpmn.dto.Process.ProcessTypeDto;
import ngvgroup.com.bpmn.dto.Process.SearchProcessDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComTxnProcessInstanceService {


    Page<ComTxnProcessDto> getProcess(PageableDTO pageableDTO);

    Page<ComTxnProcessDto> findProcess(String keyword,Pageable pageable);

    Page<ComTxnProcessDto> searchAdvance(SearchProcessDTO searchProcessDTO,Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(SearchProcessDTO searchProcessDTO, String fileName);

    List<ProcessTypeDto> getProcessTypes();


    List<String> getStates();

}
