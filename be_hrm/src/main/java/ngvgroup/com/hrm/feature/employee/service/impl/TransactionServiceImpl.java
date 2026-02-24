package ngvgroup.com.hrm.feature.employee.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.employee.dto.*;
import ngvgroup.com.hrm.feature.employee.mapper.HrmProfileMapper;
import ngvgroup.com.hrm.feature.employee.model.audit.HrmInfEmployeeA;
import ngvgroup.com.hrm.feature.employee.model.audit.HrmInfEmployeeAuthA;
import ngvgroup.com.hrm.feature.employee.model.inf.HrmInfEmployee;
import ngvgroup.com.hrm.feature.employee.model.inf.HrmInfEmployeeAuth;
import ngvgroup.com.hrm.feature.employee.model.txn.*;
import ngvgroup.com.hrm.feature.employee.repository.*;
import ngvgroup.com.hrm.feature.employee.service.TransactionService;
import ngvgroup.com.hrm.feature.employee.utils.GenerateNextSequence;
import ngvgroup.com.hrm.feature.cfg_org_unit.repository.HrmCfgOrgUnitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {
    @Getter
    private final GenerateNextSequence sequence;

    private final HrmTxnEmployeeRepository employeeRepository;
    private final HrmTxnEmployeePositionRepository positionRepository;
    private final HrmTxnEmployeeFamilyRepository familyRepository;
    private final HrmTxnEmployeeEduRepository eduRepository;
    private final HrmTxnEmployeeAuthRepository authRepository;
    private final HrmCfgOrgUnitRepository orgUnitRepository;

    // INF Repositories
    private final HrmInfEmployeeRepository infEmployeeRepository;
    private final HrmInfEmployeePositionRepository infPositionRepository;
    private final HrmInfEmployeeFamilyRepository infFamilyRepository;
    private final HrmInfEmployeeEduRepository infEduRepository;
    private final HrmInfEmployeeAuthRepository infAuthRepository;

    // AUDIT Repositories
    private final HrmInfEmployeeARepository auditEmployeeRepository;
    private final HrmInfEmployeePositionARepository auditPositionRepository;
    private final HrmInfEmployeeFamilyARepository auditFamilyRepository;
    private final HrmInfEmployeeEduARepository auditEduRepository;
    private final HrmInfEmployeeAuthARepository auditAuthRepository;

    private final HrmProfileMapper profileMapper;

    @Override
    public void createEmployee(String processTypeCode, HrmProfileDto profile) {
        HrmTxnEmployee employee = saveBasicInfo(profile.getBasicInfo(), processTypeCode, null);
        if (employee != null && profile.getBasicInfo().getEmployeeCode() == null) {
            profile.getBasicInfo().setProcessInstanceCode(employee.getProcessInstanceCode());
            profile.getBasicInfo().setEmployeeCode(employee.getEmployeeCode());
            profile.getBasicInfo().setOrgCode(employee.getOrgCode());
        }
        saveChildren(profile);
    }

    @Override
    public void updateEmployee(String processInstanceCode, HrmProfileDto profile) {
        HrmTxnEmployee employee = saveBasicInfo(profile.getBasicInfo(), null, processInstanceCode);
        if (employee != null) {
            profile.getBasicInfo().setProcessInstanceCode(employee.getProcessInstanceCode());
            profile.getBasicInfo().setEmployeeCode(employee.getEmployeeCode());
            profile.getBasicInfo().setOrgCode(employee.getOrgCode());
        }
        deleteChildren(processInstanceCode);
        saveChildren(profile);
    }

    private HrmTxnEmployee saveBasicInfo(HrmBasicInfoDto basicInfo, String processTypeCode,
            String processInstanceCode) {
        if (basicInfo == null)
            return null;

        basicInfo.setTxnDate(LocalDate.now());
        HrmTxnEmployee employee;

        if (processInstanceCode != null) {
            employee = employeeRepository.findByProcessInstanceCode(processInstanceCode)
                    .orElseThrow(() -> new BusinessException(HrmErrorCode.EMPLOYEE_TXN_NOT_FOUND, processInstanceCode));
            profileMapper.updateEmployeeFromDto(basicInfo, employee);
        } else {
            employee = profileMapper.toEmployeeEntity(basicInfo);
            employee.setProcessTypeCode(processTypeCode);
            employee.setBusinessStatus("ACTIVE");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public HrmProfileDto getProfile(String processInstanceCode) {
        HrmProfileDto profile = new HrmProfileDto();

        employeeRepository.findByProcessInstanceCode(processInstanceCode)
                .ifPresent(e -> profile.setBasicInfo(profileMapper.toBasicInfoDto(e)));

        profile.setPositionInfos(
                profileMapper.toPositionTxnDtos(positionRepository.findByProcessInstanceCode(processInstanceCode)));
        profile.setFamilyInfos(
                profileMapper.toFamilyTxnDtos(familyRepository.findByProcessInstanceCode(processInstanceCode)));
        profile.setEduInfos(profileMapper.toEduTxnDtos(eduRepository.findByProcessInstanceCode(processInstanceCode)));
        List<HrmAuthInfoDto> authInfos = profileMapper
                .toAuthTxnDtos(authRepository.findByProcessInstanceCode(processInstanceCode));
        if (authInfos != null) {
            authInfos.forEach(auth -> {
                if (auth.getAuthFromEmployeeCode() != null) {
                    // Fetch Employee Name, Mobile, ID
                    infEmployeeRepository.findByEmployeeCode(auth.getAuthFromEmployeeCode())
                            .ifPresent(emp -> {
                                auth.setAuthFromEmployeeName(emp.getEmployeeName());
                                auth.setAuthFromMobileNumber(emp.getMobileNumber());
                                auth.setAuthFromIdentificationId(emp.getIdentificationId());
                            });

                    // Fetch Position & Org Unit
                    if (auth.getAuthFromPositionCode() != null) {
                        infPositionRepository
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

        return profile;
    }

    @Override
    public void cancelRequest(String processInstanceCode) {
        employeeRepository.findByProcessInstanceCode(processInstanceCode)
                .ifPresent(employee -> {
                    employee.setBusinessStatus("CANCEL");
                    employeeRepository.save(employee);
                });
    }

    @Override
    public void updateEndProcess(String processInstanceCode) {
        HrmTxnEmployee employeeTxn = employeeRepository.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(HrmErrorCode.EMPLOYEE_TXN_NOT_FOUND, processInstanceCode));

        saveInfData(employeeTxn, processInstanceCode);
        saveAuditData(employeeTxn, processInstanceCode);
    }

    private void saveInfData(HrmTxnEmployee employeeTxn, String processInstanceCode) {
        String employeeCode = employeeTxn.getEmployeeCode();

        HrmInfEmployee inf = infEmployeeRepository.findByEmployeeCode(employeeCode)
                .map(existing -> {
                    profileMapper.updateInfFromTxn(employeeTxn, existing);
                    deleteInfChildren(employeeCode);
                    return existing;
                })
                .orElseGet(() -> profileMapper.toInfEntity(employeeTxn));
        infEmployeeRepository.save(inf);

        List<HrmTxnEmployeePosition> positionTxns = positionRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toInfPositionEntities(positionTxns), infPositionRepository);

        List<HrmTxnEmployeeFamily> familyTxns = familyRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toInfFamilyEntities(familyTxns), infFamilyRepository);

        List<HrmTxnEmployeeEdu> eduTxns = eduRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toInfEduEntities(eduTxns), infEduRepository);

        List<HrmTxnEmployeeAuth> authTxns = authRepository.findByProcessInstanceCode(processInstanceCode);
        if (authTxns != null && !authTxns.isEmpty()) {
            List<HrmInfEmployeeAuth> infAuthEntities = profileMapper.toInfAuthEntities(authTxns);
            infAuthRepository.saveAll(infAuthEntities);
        }
    }

    private void saveAuditData(HrmTxnEmployee employeeTxn, String processInstanceCode) {
        HrmInfEmployeeA employeeAudit = profileMapper.toAuditEntity(employeeTxn);
        auditEmployeeRepository.save(employeeAudit);

        List<HrmTxnEmployeePosition> positionTxns = positionRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toAuditPositionEntities(positionTxns), auditPositionRepository);

        List<HrmTxnEmployeeFamily> familyTxns = familyRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toAuditFamilyEntities(familyTxns), auditFamilyRepository);

        List<HrmTxnEmployeeEdu> eduTxns = eduRepository.findByProcessInstanceCode(processInstanceCode);
        saveIfNotEmpty(profileMapper.toAuditEduEntities(eduTxns), auditEduRepository);

        List<HrmTxnEmployeeAuth> authTxns = authRepository.findByProcessInstanceCode(processInstanceCode);
        if (authTxns != null && !authTxns.isEmpty()) {
            List<HrmInfEmployeeAuthA> auditAuthEntities = profileMapper.toAuditAuthEntities(authTxns);
            auditAuthRepository.saveAll(auditAuthEntities);
        }
    }

    private <T> void saveIfNotEmpty(List<T> entities, JpaRepository<T, ?> repository) {
        if (entities != null && !entities.isEmpty()) {
            repository.saveAll(entities);
        }
    }

    private void deleteChildren(String processInstanceCode) {
        positionRepository.deleteByProcessInstanceCode(processInstanceCode);
        familyRepository.deleteByProcessInstanceCode(processInstanceCode);
        eduRepository.deleteByProcessInstanceCode(processInstanceCode);
        authRepository.deleteByProcessInstanceCode(processInstanceCode);
    }

    private void deleteInfChildren(String employeeCode) {
        infPositionRepository.deleteByEmployeeCode(employeeCode);
        infFamilyRepository.deleteByEmployeeCode(employeeCode);
        infEduRepository.deleteByEmployeeCode(employeeCode);
        infAuthRepository.deleteByAuthEmployeeCode(employeeCode);
    }

    private void saveChildren(HrmProfileDto profile) {
        String processInstanceCode = profile.getBasicInfo().getProcessInstanceCode();
        LocalDate txnDate = profile.getBasicInfo().getTxnDate();
        String employeeCode = profile.getBasicInfo().getEmployeeCode();
        String orgCode = profile.getBasicInfo().getOrgCode();

        saveEntities(profile.getPositionInfos(),
                dtos -> profileMapper.toPositionEntities(dtos),
                entity -> {
                    entity.setEmployeeCode(employeeCode);
                    entity.setProcessInstanceCode(processInstanceCode);
                    entity.setTxnDate(txnDate);
                }, positionRepository);

        saveEntities(profile.getFamilyInfos(),
                dtos -> profileMapper.toFamilyEntities(dtos),
                entity -> {
                    entity.setOrgCode(orgCode);
                    entity.setEmployeeCode(employeeCode);
                    entity.setProcessInstanceCode(processInstanceCode);
                    entity.setTxnDate(txnDate);
                }, familyRepository);

        saveEntities(profile.getEduInfos(),
                dtos -> profileMapper.toEduEntities(dtos),
                entity -> {
                    entity.setOrgCode(orgCode);
                    entity.setEmployeeCode(employeeCode);
                    entity.setProcessInstanceCode(processInstanceCode);
                    entity.setTxnDate(txnDate);
                }, eduRepository);

        saveEntities(profile.getAuthInfos(),
                dtos -> profileMapper.toAuthEntities(dtos),
                entity -> {
                    entity.setOrgCode(orgCode);
                    entity.setAuthEmployeeCode(employeeCode);
                    entity.setProcessInstanceCode(processInstanceCode);
                    entity.setTxnDate(txnDate);
                }, authRepository);
    }

    private <D, E> void saveEntities(List<D> dtos, Function<List<D>, List<E>> mapper, Consumer<E> setter,
            JpaRepository<E, ?> repository) {
        if (dtos != null && !dtos.isEmpty()) {
            List<E> entities = mapper.apply(dtos);
            entities.forEach(setter);
            repository.saveAll(entities);
        }
    }
}
