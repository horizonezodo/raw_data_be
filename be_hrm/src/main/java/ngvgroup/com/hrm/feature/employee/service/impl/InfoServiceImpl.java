package ngvgroup.com.hrm.feature.employee.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.feature.employee.dto.EmployeeDetailDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchRequest;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchResponse;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.mapper.HrmProfileMapper;
import ngvgroup.com.hrm.feature.employee.repository.*;
import ngvgroup.com.hrm.feature.employee.service.InfoService;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.variable.TransactionHistoryDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.cfg_org_unit.repository.HrmCfgOrgUnitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    // INF Repositories
    private final HrmInfEmployeeRepository employeeRepository;
    private final HrmInfEmployeePositionRepository positionRepository;
    private final HrmInfEmployeeFamilyRepository familyRepository;
    private final HrmInfEmployeeEduRepository eduRepository;
    private final HrmInfEmployeeAuthRepository authRepository;

    // Config & Mapper
    private final HrmCfgOrgUnitRepository orgUnitRepository;
    private final HrmProfileMapper profileMapper;

    private final BpmFeignClient bpmFeignClient;

    @Override
    public Page<HrmAuthInfoSearchResponse> getEmployeeAuthSearch(HrmAuthInfoSearchRequest request, Pageable pageable) {
        return employeeRepository.searchEmployeesForAuth(request.getKeyword(), pageable);
    }

    @Override
    public EmployeeDetailDto getProfile(String employeeCode) {
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        HrmProfileDto profile = new HrmProfileDto();

        // 1. Lấy thông tin cơ bản
        employeeRepository.findByEmployeeCode(employeeCode)
                .ifPresent(e -> profile.setBasicInfo(profileMapper.toBasicInfoDto(e)));

        // 2. Lấy danh sách các thông tin phụ (Position, Family, Edu)
        profile.setPositionInfos(
                profileMapper.toPositionInfDtos(positionRepository.findByEmployeeCode(employeeCode)));
        profile.setFamilyInfos(
                profileMapper.toFamilyInfDtos(familyRepository.findByEmployeeCode(employeeCode)));
        profile.setEduInfos(
                profileMapper.toEduInfDtos(eduRepository.findByEmployeeCode(employeeCode)));

        // 3. Lấy thông tin uỷ quyền (Auth) và map thêm chi tiết người uỷ quyền
        // Lưu ý: Tên hàm repository phụ thuộc vào cách bạn đặt, ở đây giả định là
        // findByAuthEmployeeCode
        // dựa trên hàm deleteByAuthEmployeeCode bên TransactionService.
        List<HrmAuthInfoDto> authInfos = profileMapper
                .toAuthInfDtos(authRepository.findByAuthEmployeeCode(employeeCode));

        if (authInfos != null) {
            authInfos.forEach(auth -> {
                if (auth.getAuthFromEmployeeCode() != null) {
                    // Fetch Employee Name, Mobile, ID
                    employeeRepository.findByEmployeeCode(auth.getAuthFromEmployeeCode())
                            .ifPresent(emp -> {
                                auth.setAuthFromEmployeeName(emp.getEmployeeName());
                                auth.setAuthFromMobileNumber(emp.getMobileNumber());
                                auth.setAuthFromIdentificationId(emp.getIdentificationId());
                            });

                    // Fetch Position & Org Unit
                    if (auth.getAuthFromPositionCode() != null) {
                        positionRepository
                                .findByEmployeeCodeAndPositionCode(auth.getAuthFromEmployeeCode(),
                                        auth.getAuthFromPositionCode())
                                .ifPresent(pos -> {
                                    auth.setAuthFromOrgUnitCode(pos.getOrgUnitCode());
                                    orgUnitRepository.findByOrgUnitCode(pos.getOrgUnitCode())
                                            .ifPresent(org -> auth.setAuthFromOrgUnitName(org.getOrgUnitName()));
                                });
                    }
                }
            });
        }
        profile.setAuthInfos(authInfos);

        employeeDetailDto.setProfile(profile);
        List<ProcessFileDto> files = bpmFeignClient.getProcessFilesFromReferenceCode(employeeCode,
                HrmVariableConstants.PROCESS_KEY_HRM_ADJUST).getData();
        employeeDetailDto.setFiles(files);
        List<TransactionHistoryDto> histories = bpmFeignClient.getTransactionHistory(employeeCode,
                List.of(HrmVariableConstants.PROCESS_KEY_HRM_ADJUST, HrmVariableConstants.PROCESS_KEY_HRM_REGISTER))
                .getData();
        employeeDetailDto.setHistories(histories);
        return employeeDetailDto;
    }
}