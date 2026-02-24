package com.naas.admin_service.features.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO;
import com.naas.admin_service.features.category.dto.ExportCtgInfCreditInstDTO;
import com.naas.admin_service.features.category.model.CtgInfCreditInst;

import java.util.List;

@Repository
public interface CtgInfCreditInstRepository extends JpaRepository<CtgInfCreditInst,Long> {
    @Query("""
    select new com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO(
        c.id, c.creditInstCode, c.creditInstName, c.creditInstShortName, c.address,
        c.phoneNumber, c.email, c.website, c.taxCode, c.licenseNo,
        c.licenseDate,c.isActive, c.description ,case when c.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end)
    from CtgInfCreditInst c
    where (:keyword is null or :keyword = ''
        or LOWER(c.creditInstCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.creditInstName) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.address) like concat('%', LOWER(:keyword), '%')
        or (c.isActive = 1 and LOWER('hiệu lực') like concat('%', LOWER(:keyword), '%'))
        or (c.isActive = 0 and LOWER('hết hiệu lực') like concat('%', LOWER(:keyword), '%'))
    )
    order by c.modifiedDate desc
""")
    Page<CtgInfCreditInstDTO> search(@Param("keyword")String keyword, Pageable pageable);


    @Query("""
    select new com.naas.admin_service.features.category.dto.ExportCtgInfCreditInstDTO(
        c.creditInstCode, c.creditInstName, c.address, case when c.isActive = 1 then 'Hiệu lực' else 'Hết hiệu lực' end)
    from CtgInfCreditInst c""")
    List<ExportCtgInfCreditInstDTO> exportData();

    CtgInfCreditInst findByCreditInstCode(String creditInstCode);

    @Query("select c from CtgInfCreditInst  c where c.isDelete = :isDelete and (c.creditInstCode = :creditInstCode or c.creditInstName = :creditInstName)")
    CtgInfCreditInst findCreditInts(@Param("creditInstCode") String creditInstCode,@Param("creditInstName") String creditInstName,@Param("isDelete") int isDelete);

    boolean existsByCreditInstCode(String creditInstCode);
}
