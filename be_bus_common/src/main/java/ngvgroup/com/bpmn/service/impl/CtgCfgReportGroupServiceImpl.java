package ngvgroup.com.bpmn.service.impl;

import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpmn.dto.CtgCfgReportGroup.CtgCfgReportGroupDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportGroupDTO.CtgCfgReportGroupDTO;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import ngvgroup.com.bpmn.exception.BusCommonErrorCode;
import ngvgroup.com.bpmn.mapper.CtgCfgReportGroup.CtgCfgReportGroupMapper;
import ngvgroup.com.bpmn.model.CtgCfgReport;
import ngvgroup.com.bpmn.model.CtgCfgReportGroup;
import ngvgroup.com.bpmn.repository.CtgCfgReportGroupRepository;
import ngvgroup.com.bpmn.repository.CtgCfgReportRepository;
import ngvgroup.com.bpmn.service.CtgCfgReportGroupService;
import ngvgroup.com.bpmn.utils.PageUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CtgCfgReportGroupServiceImpl implements CtgCfgReportGroupService {
    private final CtgCfgReportGroupRepository ctgCfgReportGroupRepository;
    private final ExcelService excelService;
    private final CtgCfgReportGroupMapper ctgCfgReportGroupMapper;
    private final CtgCfgReportRepository ctgCfgReportRepository;

    @Override
    public Page<CtgCfgReportGroupDto> getListReportGroups(Pageable pageable) {


        return ctgCfgReportGroupRepository.getListReportGroup(pageable);
    }

    @Override
    public Page<CtgCfgReportGroupDto> findListReportGroups(SearchFilterRequest searchFilterRequest){
        Pageable pageable=PageUtils.toPageable(searchFilterRequest.getPageable());
        return ctgCfgReportGroupRepository.findListReportGroupDto(searchFilterRequest.getFilter(), pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(CtgCfgReportGroupDto ctgCfgReportGroupDto, String fileName) {
        Pageable pageable=PageUtils.toPageable(ctgCfgReportGroupDto.getSearchFilter().getPageable());
        List<CtgCfgReportGroupDto> ctgCfgReportGroupDtos = ctgCfgReportGroupRepository.exportToExcel(
                ctgCfgReportGroupDto.getSearchFilter().getFilter(),
                pageable
        );

        byte[] response = excelService.exportToExcelContent(ctgCfgReportGroupDtos, ctgCfgReportGroupDto.getLabels(), CtgCfgReportGroupDto.class);

        try {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName +".xlsx");
            return new ResponseEntity<>(response,
                    header, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    @Override
    public void createReportGroup(CtgCfgReportGroupDto ctgCfgReportGroupDto)  {

        Optional<CtgCfgReportGroup> cfgReportGroup= ctgCfgReportGroupRepository.findComCfgReportGroupByReportGroupCode(ctgCfgReportGroupDto.getReportGroupCode());
        if(cfgReportGroup.isPresent()){


            throw new BusinessException(ErrorCode.CONFLICT, ctgCfgReportGroupDto.getReportGroupName());

        }

        CtgCfgReportGroup ctgCfgReportGroup =new CtgCfgReportGroup(
                ctgCfgReportGroupDto.getReportGroupCode(),
                ctgCfgReportGroupDto.getSortNumber(),
                ctgCfgReportGroupDto.getReportGroupName(),
                ctgCfgReportGroupDto.getNameEng(),
                ctgCfgReportGroupDto.getDescription()
        );

        ctgCfgReportGroupRepository.save(ctgCfgReportGroup);

    }

    @Override
    public void updateReportGroup(CtgCfgReportGroupDto ctgCfgReportGroupDto)  {

        Optional<CtgCfgReportGroup> cfgReportGroup= ctgCfgReportGroupRepository.findComCfgReportGroupByReportGroupCode(ctgCfgReportGroupDto.getReportGroupCode());
        if(!cfgReportGroup.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND, ctgCfgReportGroupDto.getReportGroupName());
        }
        cfgReportGroup.get().setReportGroupName(ctgCfgReportGroupDto.getReportGroupName());
        cfgReportGroup.get().setDescription(ctgCfgReportGroupDto.getDescription());
        cfgReportGroup.get().setSortNumber(ctgCfgReportGroupDto.getSortNumber());
        cfgReportGroup.get().setReportGroupNameEn(ctgCfgReportGroupDto.getNameEng());
        ctgCfgReportGroupRepository.save(cfgReportGroup.get());

    }

    @Override
    public CtgCfgReportGroupDto getInfoReportGroup(String reportGroupCode) {
        return ctgCfgReportGroupRepository.getInfoReportGroup(reportGroupCode);
    }

    @Transactional
    @Override
    public void deleteReportGroup(String reportGroupCode)  {
        Optional<CtgCfgReportGroup> cfgReportGroup= ctgCfgReportGroupRepository.findComCfgReportGroupByReportGroupCode(reportGroupCode);
        if(!cfgReportGroup.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND, reportGroupCode);
        }

        Optional<CtgCfgReport> ctgCfgReport=ctgCfgReportRepository.findCtgCfgReportByReportGroupCode(reportGroupCode);
        if(ctgCfgReport.isPresent()){
            throw new BusinessException(BusCommonErrorCode.REPORT_GROUP_CODE_ALREADY_EXISTS, reportGroupCode);
        }
        ctgCfgReportGroupRepository.deleteComCfgReportGroupByReportGroupCode(reportGroupCode);
    }

    @Override
    public List<CtgCfgReportGroupDto>getListReportGroups(){
        return ctgCfgReportGroupRepository.getListReportGroupDto();
    }

    @Override
    public List<CtgCfgReportGroupDTO> getAll() {
        return ctgCfgReportGroupMapper.toDto(ctgCfgReportGroupRepository.findAll());
    }

    @Override
    public boolean checkExist(String code){
        Optional<CtgCfgReportGroup> cfgReportGroup= ctgCfgReportGroupRepository.findComCfgReportGroupByReportGroupCode(code);
        if(!cfgReportGroup.isPresent()){
            return false;
        }
        return true;
    }
}
