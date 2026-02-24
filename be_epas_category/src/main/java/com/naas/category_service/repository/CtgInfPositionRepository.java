package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgInfPosition.ExportCtgInfPositionDTO;
import com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO;
import com.naas.category_service.model.CtgInfPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgInfPositionRepository extends JpaRepository<CtgInfPosition,Long> {
    @Query("""
    select new com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO(
        h.id, h.orgCode, h.positionCode, h.positionName, h.isActive, h.description,
        case when h.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end)
    from CtgInfPosition h
    where (:keyword is null or :keyword = ''
        or LOWER(h.positionCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(h.positionName) like concat('%', LOWER(:keyword), '%')
        or (h.isActive = 1 and LOWER('hiệu lực') like concat('%', LOWER(:keyword), '%'))
        or (h.isActive = 0 and LOWER('hết hiệu lực') like concat('%', LOWER(:keyword), '%'))
    )
    order by h.modifiedDate desc
""")
    Page<CtgInfPositionDTO> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.naas.category_service.dto.CtgInfPosition.ExportCtgInfPositionDTO(h.positionCode, h.positionName, case when h.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end) from CtgInfPosition h")
    List<ExportCtgInfPositionDTO> exportData();
    CtgInfPosition findByPositionCode(String positionCode);

    @Query("SELECT p FROM CtgInfPosition p where p.isDelete = :isDelete and (p.positionName = :positionName or p.positionCode = :positionCOde)")
    CtgInfPosition checkPosition(@Param("positionCOde") String positionCOde, @Param("positionName") String positionName, @Param("isDelete")int isDelete);

    boolean existsByPositionCode(String positionCode);
}
