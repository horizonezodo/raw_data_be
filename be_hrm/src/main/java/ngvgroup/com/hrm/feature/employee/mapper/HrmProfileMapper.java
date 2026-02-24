package ngvgroup.com.hrm.feature.employee.mapper;

import ngvgroup.com.hrm.feature.employee.dto.*;
import ngvgroup.com.hrm.feature.employee.model.audit.*;
import ngvgroup.com.hrm.feature.employee.model.inf.*;
import ngvgroup.com.hrm.feature.employee.model.txn.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HrmProfileMapper {

    // Basic Info mappings
    HrmTxnEmployee toEmployeeEntity(HrmBasicInfoDto dto);

    HrmBasicInfoDto toBasicInfoDto(HrmTxnEmployee entity);

    HrmBasicInfoDto toBasicInfoDto(HrmInfEmployee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEmployeeFromDto(HrmBasicInfoDto dto, @MappingTarget HrmTxnEmployee entity);

    // Position Info mappings
    HrmTxnEmployeePosition toPositionEntity(HrmPositionInfoDto dto);

    HrmPositionInfoDto toPositionDto(HrmTxnEmployeePosition entity);

    List<HrmTxnEmployeePosition> toPositionEntities(List<HrmPositionInfoDto> dtos);

    List<HrmPositionInfoDto> toPositionTxnDtos(List<HrmTxnEmployeePosition> entities);

    HrmPositionInfoDto toPositionDto(HrmInfEmployeePosition entity);

    List<HrmPositionInfoDto> toPositionInfDtos(List<HrmInfEmployeePosition> entities);

    // Family Info mappings
    HrmTxnEmployeeFamily toFamilyEntity(HrmFamilyInfoDto dto);

    HrmFamilyInfoDto toFamilyDto(HrmTxnEmployeeFamily entity);

    List<HrmTxnEmployeeFamily> toFamilyEntities(List<HrmFamilyInfoDto> dtos);

    List<HrmFamilyInfoDto> toFamilyTxnDtos(List<HrmTxnEmployeeFamily> entities);

    HrmFamilyInfoDto toFamilyDto(HrmInfEmployeeFamily entity);

    List<HrmFamilyInfoDto> toFamilyInfDtos(List<HrmInfEmployeeFamily> entities);

    // Edu Info mappings
    HrmTxnEmployeeEdu toEduEntity(HrmEduInfoDto dto);

    HrmEduInfoDto toEduDto(HrmTxnEmployeeEdu entity);

    List<HrmTxnEmployeeEdu> toEduEntities(List<HrmEduInfoDto> dtos);

    List<HrmEduInfoDto> toEduTxnDtos(List<HrmTxnEmployeeEdu> entities);

    HrmEduInfoDto toEduDto(HrmInfEmployeeEdu entity);

    List<HrmEduInfoDto> toEduInfDtos(List<HrmInfEmployeeEdu> entities);

    // Auth Info mappings
    HrmTxnEmployeeAuth toAuthEntity(HrmAuthInfoDto dto);

    HrmAuthInfoDto toAuthDto(HrmTxnEmployeeAuth entity);

    List<HrmTxnEmployeeAuth> toAuthEntities(List<HrmAuthInfoDto> dtos);

    List<HrmAuthInfoDto> toAuthTxnDtos(List<HrmTxnEmployeeAuth> entities);

    // THÊM MỚI: Map từ INF Entity sang DTO cho Auth
    HrmAuthInfoDto toAuthDto(HrmInfEmployeeAuth entity);

    List<HrmAuthInfoDto> toAuthInfDtos(List<HrmInfEmployeeAuth> entities);

    // TXN to INF mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    HrmInfEmployee toInfEntity(HrmTxnEmployee txn);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateInfFromTxn(HrmTxnEmployee txn, @MappingTarget HrmInfEmployee inf);

    HrmInfEmployeePosition toInfPositionEntity(HrmTxnEmployeePosition txn);

    List<HrmInfEmployeePosition> toInfPositionEntities(List<HrmTxnEmployeePosition> txn);

    HrmInfEmployeeFamily toInfFamilyEntity(HrmTxnEmployeeFamily txn);

    List<HrmInfEmployeeFamily> toInfFamilyEntities(List<HrmTxnEmployeeFamily> txn);

    HrmInfEmployeeEdu toInfEduEntity(HrmTxnEmployeeEdu txn);

    List<HrmInfEmployeeEdu> toInfEduEntities(List<HrmTxnEmployeeEdu> txn);

    HrmInfEmployeeAuth toInfAuthEntity(HrmTxnEmployeeAuth txn);

    List<HrmInfEmployeeAuth> toInfAuthEntities(List<HrmTxnEmployeeAuth> txn);

    // TXN to AUDIT mappings
    @Mapping(target = "dataTime", expression = "java(java.time.LocalDateTime.now())")
    HrmInfEmployeeA toAuditEntity(HrmTxnEmployee txn);

    @Mapping(target = "dataTime", expression = "java(java.time.LocalDateTime.now())")
    HrmInfEmployeePositionA toAuditPositionEntity(HrmTxnEmployeePosition txn);

    List<HrmInfEmployeePositionA> toAuditPositionEntities(List<HrmTxnEmployeePosition> txn);

    @Mapping(target = "dataTime", expression = "java(java.time.LocalDateTime.now())")
    HrmInfEmployeeFamilyA toAuditFamilyEntity(HrmTxnEmployeeFamily txn);

    List<HrmInfEmployeeFamilyA> toAuditFamilyEntities(List<HrmTxnEmployeeFamily> txn);

    @Mapping(target = "dataTime", expression = "java(java.time.LocalDateTime.now())")
    HrmInfEmployeeEduA toAuditEduEntity(HrmTxnEmployeeEdu txn);

    List<HrmInfEmployeeEduA> toAuditEduEntities(List<HrmTxnEmployeeEdu> txn);

    @Mapping(target = "dataTime", expression = "java(java.time.LocalDateTime.now())")
    HrmInfEmployeeAuthA toAuditAuthEntity(HrmTxnEmployeeAuth txn);

    List<HrmInfEmployeeAuthA> toAuditAuthEntities(List<HrmTxnEmployeeAuth> txn);
}