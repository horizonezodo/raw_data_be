package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.SelectBoxDto;
import com.ngv.zns_service.dto.response.dvu.ZSSLoaiDvuDto;
import com.ngv.zns_service.model.entity.ZSSDmucCtiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSDmucCtietRepository extends JpaRepository<ZSSDmucCtiet, String> {
    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc IN (:maDmuc, 'Tất cả')")
    List<ZSSDmucCtiet> listCommon(@Param("maDmuc") String maDmuc);
    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc IN ('TTHAI_DVU', 'Tất cả')")
    List<ZSSDmucCtiet> listTthai();
    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc IN ('LOAI_ND_ZNS', 'Tất cả')")
    List<ZSSDmucCtiet> listLoaiNdungMau();
    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc IN ('TTHAI_ZNS', 'Tất cả')")
    List<ZSSDmucCtiet> listTthaiMau();

    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc = 'LOAI_ND_ZNS' AND m.maDmucCtiet = '1'" )
    List<ZSSDmucCtiet> listMucDichGui();

    @Query("SELECT m FROM ZSSDmucCtiet m WHERE m.maDmuc = 'LOAI_BUTTON' AND m.maDmucCtiet IN ('1', '2', '3')")
    List<ZSSDmucCtiet> listLoaiButton();

    @Query("SELECT new com.ngv.zns_service.dto.response.dvu.ZSSLoaiDvuDto(" +
            "dm.maDmucCtiet, dm.tenDmucCtiet)" +
            "FROM ZSSDmucCtiet dm " +
            "WHERE dm.maDmuc = 'LOAI_DVU' " +
            "AND (:maDmCtiet IS NULL OR dm.maDmucCtiet = :maDmCtiet)")
    List<ZSSLoaiDvuDto> findLoaiDvu(@Param("maDmCtiet") String maDmCtiet);

    @Query("SELECT new com.ngv.zns_service.dto.response.SelectBoxDto(" +
            "dm.maDmucCtiet, dm.tenDmucCtiet)" +
            "FROM ZSSDmucCtiet dm " +
            "WHERE dm.maDmuc = 'MDICH_SDUNG'")
    List<SelectBoxDto> findMdichSdungSelectBox();
}
