package ngvgroup.com.bpmn.controller;

import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;


import ngvgroup.com.bpmn.dto.Process.ComTxnProcessDto;

import ngvgroup.com.bpmn.dto.Process.ProcessTypeDto;
import ngvgroup.com.bpmn.dto.Process.SearchProcessDTO;
import ngvgroup.com.bpmn.service.ComTxnProcessInstanceService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/process")
public class ComTxnProcessInstanceController {
    private final ComTxnProcessInstanceService comTxnProcessInstanceService;


    @PostMapping("/get")
    @PreAuthorize("hasRole('impl_plan_search')")
    public ResponseEntity<ResponseData<Page<ComTxnProcessDto>>> getProcess(@RequestBody PageableDTO pageableDTO){
        return ResponseData.okEntity(comTxnProcessInstanceService.getProcess(pageableDTO));
    }

    @PostMapping("/find")
    @PreAuthorize("hasRole('impl_plan_search')")
    public ResponseEntity<ResponseData<Page<ComTxnProcessDto>>> findProcess(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(comTxnProcessInstanceService.findProcess(keyword,pageable));
    }

    @PostMapping("/search-advance")
    @PreAuthorize("hasRole('impl_plan_search')")
    public ResponseEntity<ResponseData<Page<ComTxnProcessDto>>> searchAdvance(@RequestBody SearchProcessDTO searchProcessDTO,@ParameterObject Pageable pageable){
        return ResponseData.okEntity(comTxnProcessInstanceService.searchAdvance(searchProcessDTO,pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    @PreAuthorize("hasRole('impl_plan_search')")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestBody SearchProcessDTO  searchProcessDTO, @PathVariable("fileName")String fileName){
        return comTxnProcessInstanceService.exportToExcel(searchProcessDTO,fileName);
    }

    @GetMapping("/get-process-type")
    public ResponseEntity<ResponseData<List<ProcessTypeDto>>> getProcessTypes(){
        return  ResponseData.okEntity(comTxnProcessInstanceService.getProcessTypes());
    }


    @GetMapping("/get-states")
    public ResponseEntity<ResponseData<List<String>>> getStates(){
        return  ResponseData.okEntity(comTxnProcessInstanceService.getStates());
    }

}
