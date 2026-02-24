package ngvgroup.com.rpt.features.ctgcfgstatus.repository;

import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatusRepository extends JpaRepository<CtgCfgStatus, Long> {
    Optional<CtgCfgStatus> findByStatusCode(String statusCode);

    @Query("SELECT c.statusName FROM CtgCfgStatus c WHERE c.statusCode = :statusCode")
    String findStatusName(@Param("statusCode") String statusCode);

    CtgCfgStatus findByStatusCodeAndIsDelete(String statusCode, Integer isDelete);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto(
                    c.statusCode,
                    c.statusName,
                    c.statusTypeCode,
                    COALESCE(cs.commonName, ''),
                    c.description,
                    c.isActive
                ) FROM CtgCfgStatus c
                LEFT JOIN ComCfgCommon cs
                ON c.statusTypeCode = cs.commonCode AND cs.commonTypeCode = 'CM018'
                WHERE c.isDelete = 0
                AND (:keyword IS NULL OR
                LOWER(c.statusCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.statusName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.statusTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(cs.commonName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ORDER BY c.sortNumber ASC
            """)
    Page<CtgCfgStatusDto> pageStatusWithType(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgstatus.dto.ExportExcelData(
                    c.statusCode,
                    c.statusName,
                    c.statusTypeCode,
                    COALESCE(cs.commonName, ''),
                    c.description
                ) FROM CtgCfgStatus c
                LEFT JOIN ComCfgCommon cs
                ON c.statusTypeCode = cs.commonCode AND cs.commonTypeCode = 'CM018'
                WHERE c.isDelete = 0
                AND (:keyword IS NULL OR
                LOWER(c.statusCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.statusName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.statusTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(cs.commonName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ORDER BY c.sortNumber ASC
            """)
    List<ExportExcelData> listStatusWithType(@Param("keyword") String keyword);

    boolean existsByStatusCodeAndIsDelete(String statusCode, Integer isDelete);

    @Query("select s.statusCode from CtgCfgStatus s")
    List<String> getAllStatusCode();


    List<CtgCfgStatus> findByIsDeleteOrderBySortNumberAsc(Integer isDelete);
    List<CtgCfgStatus> findByAndStatusTypeCodeAndIsDeleteOrderBySortNumberAsc(String type , Integer isDelete);

}
