package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.dvu.CtDvMapGdvDto;
import com.ngv.zns_service.dto.response.goiDv.GoiDvuDetailMappingDvuDto;
import com.ngv.zns_service.dto.response.goiDv.GoiDvuDetailTTinChungDto;
import com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuExcelDto;
import com.ngv.zns_service.dto.response.goiDv.ZSSTthaiHdongDto;
import com.ngv.zns_service.model.entity.ZSSGoiDvu;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSGoiDvuRepository extends JpaRepository<ZSSGoiDvu, String> {
        List<ZSSGoiDvu> findAllByMaGoiDvuIn(List<String> maGoiDvu);

        @Query(value = "SELECT * FROM ZSS_GOI_DVU gvd\n" +
                        "WHERE gvd.MA_GOI_DVU  = :gdv\n" +
                        "ORDER BY gvd.MA_GOI_DVU", nativeQuery = true)
        List<ZSSGoiDvu> getAll(@Param("gdv") String gdv);

        Page<ZSSGoiDvu> findAll(Pageable pageable);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_GOI_DVU g WHERE g.MA_GOI_DVU IN :ids", nativeQuery = true)
        void deleteByMaGoiDvuList(@Param("ids") List<String> ids);

        @Query("SELECT new com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuExcelDto(" +
                        "gdv.maGoiDvu, gdv.tenGoiDvu, gdv.mota, gdv.tthaiNvu, gdv.ngayNhap) " +
                        "FROM ZSSGoiDvu gdv ")
        List<ZSSGoiDvuExcelDto> exportExcel();

        @Query("Select new com.ngv.zns_service.dto.response.goiDv.GoiDvuDetailTTinChungDto ( " +
                        "gdv.maGoiDvu, gdv.tenGoiDvu, gdv.tthaiNvu, gdv.mota) " +
                        "FROM ZSSGoiDvu gdv " +
                        "WHERE gdv.maGoiDvu = :mgdv ")
        GoiDvuDetailTTinChungDto findGoiDvu(@Param("mgdv") String mgdv);

        @Query("Select new com.ngv.zns_service.dto.response.goiDv.GoiDvuDetailMappingDvuDto ( " +
                        "ct.maDvu, dv.tenDvu, dv.maLoaiDvu, dm.tenDmucCtiet ) " +
                        "FROM ZSSDvu dv " +
                        "INNER JOIN ZSSGoiDvuCTiet ct ON dv.maDvu = ct.maDvu " +
                        "INNER JOIN ZSSDmucCtiet dm ON dv.maLoaiDvu = dm.maDmucCtiet " +
                        "WHERE :mgdv IS NULL OR ct.maGoiDvu = :mgdv")
        List<GoiDvuDetailMappingDvuDto> findMappingGoiDvu(@Param("mgdv") String mgdv);

        boolean existsAllByMaGoiDvu(String mgdv);

        @Query("SELECT new com.ngv.zns_service.dto.response.goiDv.ZSSTthaiHdongDto(" +
                        "dm.maDmucCtiet, dm.tenDmucCtiet)" +
                        "FROM ZSSDmucCtiet dm " +
                        "WHERE dm.maDmuc = 'TTHAI_DVU' " +
                        "AND (:mdm IS NULL OR dm.maDmucCtiet = :mdm )")
        List<ZSSTthaiHdongDto> listTthaiHd(@Param("mdm") String mdm);

        @Query("SELECT new com.ngv.zns_service.dto.response.dvu.CtDvMapGdvDto(" +
                        "zgdc.maGoiDvu , zgd.tenGoiDvu , zgd.mota , zgd.tthaiNvu , zgd.ngayNhap )" +
                        "FROM ZSSGoiDvu zgd " +
                        "INNER JOIN ZSSGoiDvuCTiet zgdc ON zgd.maGoiDvu= zgdc.maGoiDvu " +
                        "WHERE zgdc.maDvu = :mdv ")
        List<CtDvMapGdvDto> listMappingGdv(@Param("mdv") String mdv);
}
