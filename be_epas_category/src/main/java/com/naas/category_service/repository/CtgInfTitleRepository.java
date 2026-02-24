package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgInfTitle.ExportCtgInfTitleDTO;
import com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO;
import com.naas.category_service.model.CtgInfTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgInfTitleRepository extends JpaRepository<CtgInfTitle, Long> {
    @Query("""
    select new com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO(
        c.id, c.orgCode, c.titleCode, c.titleName, c.positionCode, p.positionName,
        c.investorRole, c.educaionLevel, c.functionTask, c.isActive, c.description,
        case when c.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end)
    from CtgInfTitle c 
    join CtgInfPosition p on c.positionCode = p.positionCode
    join ComInfOrganization o on c.orgCode = o.orgCode
    where (:keyword is null or :keyword = '' 
        or LOWER(c.titleCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.titleName) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.orgCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(o.orgName) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.positionCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(p.positionName) like concat('%', LOWER(:keyword), '%')
        or (c.isActive = 1 and LOWER('hiệu lực') like concat('%', LOWER(:keyword), '%'))
        or (c.isActive = 0 and LOWER('hết hiệu lực') like concat('%', LOWER(:keyword), '%'))
    )
    order by c.modifiedDate desc
""")
    Page<CtgInfTitleDTO> search(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
    select new com.naas.category_service.dto.CtgInfTitle.ExportCtgInfTitleDTO(
        c.titleCode, c.titleName, p.positionName,o.orgName, case when c.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end)
    from CtgInfTitle c inner join CtgInfPosition p on c.positionCode = p.positionCode inner join ComInfOrganization o on c.orgCode = o.orgCode""")
    List<ExportCtgInfTitleDTO> exportData();

    CtgInfTitle findByTitleCode(String titleCode);

    @Query("select c from CtgInfTitle  c where c.isDelete = :isDelete and (c.titleCode = :titleCode or c.titleName = :titleName)")
    CtgInfTitle findTitle(@Param("titleCode") String titleCode, @Param("titleName") String titleName, @Param("isDelete") int isDelete);

    boolean existsByTitleCode(String titleCode);

    List<CtgInfTitle> findAllByPositionCode(String positionCode);
}
