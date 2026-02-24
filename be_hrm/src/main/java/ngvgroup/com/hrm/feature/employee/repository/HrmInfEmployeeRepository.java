package ngvgroup.com.hrm.feature.employee.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeListDTO;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeSearchRequest;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchResponse;
import ngvgroup.com.hrm.feature.employee.model.inf.HrmInfEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HrmInfEmployeeRepository extends BaseRepository<HrmInfEmployee> {

    @Query("""
            select new ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeListDTO(
                e.employeeCode,
                e.employeeName,
                e.mobileNumber,
                e.identificationId,
                pos.positionName,
                org.orgUnitName,
                st.statusName
            )
            from HrmInfEmployee e
            left join HrmInfEmployeePosition ep
                on ep.employeeCode = e.employeeCode
                and ep.validFrom = (
                    select max(p2.validFrom)
                    from HrmInfEmployeePosition p2
                    where p2.employeeCode = e.employeeCode
                )
            left join HrmCfgPosition pos
                on pos.positionCode = ep.positionCode
            left join HrmCfgOrgUnit org
                on org.orgUnitCode = ep.orgUnitCode
            left join HrmCfgStatus st
                on st.statusCode = e.statusCode
            where e.isDelete = 0
            and (
                :#{#req.orgCode} is null
                or e.orgCode = :#{#req.orgCode}
            )
            and (
                :#{#req.orgUnitCodes} is null
                or ep.orgUnitCode in (:#{#req.orgUnitCodes})
            )
            and (
                :#{#req.employeeTypeCodes} is null
                or e.employeeTypeCode in (:#{#req.employeeTypeCodes})
            )
            and (
                :#{#req.statusCodes} is null
                or e.statusCode in (:#{#req.statusCodes})
            )
            and (
                :#{#req.keyword} is null
                or lower(e.employeeCode) like
                    concat('%', lower(:#{#req.keyword}), '%')
                or lower(e.employeeName) like
                    concat('%', lower(:#{#req.keyword}), '%')
                or lower(e.mobileNumber) like
                    concat('%', lower(:#{#req.keyword}), '%')
                or lower(e.identificationId) like
                    concat('%', lower(:#{#req.keyword}), '%')
            )
            order by e.modifiedDate desc
            """)
    Page<HrmInfEmployeeListDTO> search(@Param("req") HrmInfEmployeeSearchRequest req, Pageable pageable);

    Optional<HrmInfEmployee> findByEmployeeCode(String employeeCode);

    boolean existsByMobileNumberAndEmployeeCodeNot(String mobileNumber, String employeeCode);

    boolean existsByIdentificationIdAndEmployeeCodeNot(String identificationId, String employeeCode);

    @Query("""
            select new ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchResponse(
                e.employeeCode,
                e.employeeName,
                e.mobileNumber,
                e.identificationId,
                o.orgUnitCode,
                o.orgUnitName,
                p.positionCode,
                pos.positionName
            )
            from HrmInfEmployee e
            left join HrmInfEmployeePosition p on e.employeeCode = p.employeeCode
                and p.validFrom = (
                    select max(p2.validFrom)
                    from HrmInfEmployeePosition p2
                    where p2.employeeCode = e.employeeCode
                )
            left join HrmCfgOrgUnit o on p.orgUnitCode = o.orgUnitCode
            left join HrmCfgPosition pos on p.positionCode = pos.positionCode
            where e.isDelete = 0
            and (
                :#{#keyword} is null
                or lower(e.employeeCode) like concat('%', lower(:#{#keyword}), '%')
                or lower(e.employeeName) like concat('%', lower(:#{#keyword}), '%')
                or lower(e.mobileNumber) like concat('%', lower(:#{#keyword}), '%')
                or lower(e.identificationId) like concat('%', lower(:#{#keyword}), '%')
                or lower(o.orgUnitName) like concat('%', lower(:#{#keyword}), '%')
                or lower(pos.positionName) like concat('%', lower(:#{#keyword}), '%')
            )
            order by e.employeeName asc
            """)
    Page<HrmAuthInfoSearchResponse> searchEmployeesForAuth(@Param("keyword") String keyword, Pageable pageable);
}
