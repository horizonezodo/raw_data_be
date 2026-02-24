package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse;
import com.ngv.zns_service.model.entity.ZSSDviThueBao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSDviThueBaoRepository extends JpaRepository<ZSSDviThueBao, String> {
    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse(" +
            "tb.maThueBao, tb.maDvi, tb.zoaId, dvi.tenDvi, tb.maGoiDvu, dv.tenGoiDvu, tb.ngayDky,tb.ngayHluc, tb.tgian, tb.ngayHetHluc, dmct.tenDmucCtiet) " +
            "FROM ZSSDviThueBao tb " +
            "LEFT JOIN ZSSGoiDvu dv ON tb.maGoiDvu = dv.maGoiDvu " +
            "LEFT JOIN DCDvi dvi ON tb.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet dmct ON tb.tthaiNvu = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_DVU' " +
            "WHERE (:maGoiDvu IS NULL OR dv.maGoiDvu = :maGoiDvu) " +
            "AND (:tthaiNvu IS NULL OR dmct.maDmucCtiet = :tthaiNvu) " +
            "AND (:tuNgayDky IS NULL OR tb.ngayDky >= :tuNgayDky) " +
            "AND (:denNgayDky IS NULL OR tb.ngayDky <= :denNgayDky) " +
            "AND (:tuNgayHetHluc IS NULL OR tb.ngayHetHluc >= :tuNgayHetHluc) " +
            "AND (:denNgayHetHluc IS NULL OR tb.ngayHetHluc <= :denNgayHetHluc)")
    Page<ZSSDviThueBaoResponse> search(@Param("maGoiDvu") String maGoiDvu,
                                       @Param("tthaiNvu") String tthaiNvu,
                                       @Param("tuNgayDky") String tuNgayDky,
                                       @Param("denNgayDky") String denNgayDky,
                                       @Param("tuNgayHetHluc") String tuNgayHetHluc,
                                       @Param("denNgayHetHluc") String denNgayHetHluc,
                                       Pageable pageable);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse(" +
            "tb.maThueBao, tb.maDvi, tb.zoaId, dvi.tenDvi, tb.maGoiDvu, dv.tenGoiDvu, tb.ngayDky,tb.ngayHluc, tb.tgian, tb.ngayHetHluc, dmct.tenDmucCtiet) " +
            "FROM ZSSDviThueBao tb " +
            "LEFT JOIN ZSSGoiDvu dv ON tb.maGoiDvu = dv.maGoiDvu " +
            "LEFT JOIN DCDvi dvi ON tb.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet dmct ON tb.tthaiNvu = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_DVU' " +
            "WHERE (:maGoiDvu IS NULL OR dv.maGoiDvu = :maGoiDvu) " +
            "AND (:tthaiNvu IS NULL OR dmct.maDmucCtiet = :tthaiNvu) " +
            "AND (:tuNgayDky IS NULL OR tb.ngayDky >= :tuNgayDky) " +
            "AND (:denNgayDky IS NULL OR tb.ngayDky <= :denNgayDky) " +
            "AND (:tuNgayHetHluc IS NULL OR tb.ngayHetHluc >= :tuNgayHetHluc) " +
            "AND (:denNgayHetHluc IS NULL OR tb.ngayHetHluc <= :denNgayHetHluc)")
    List<ZSSDviThueBaoResponse> searchToExportExcel(@Param("maGoiDvu") String maGoiDvu,
                                       @Param("tthaiNvu") String tthaiNvu,
                                       @Param("tuNgayDky") String tuNgayDky,
                                       @Param("denNgayDky") String denNgayDky,
                                       @Param("tuNgayHetHluc") String tuNgayHetHluc,
                                       @Param("denNgayHetHluc") String denNgayHetHluc);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse(" +
            "tb.maThueBao, tb.maDvi, tb.zoaId, dvi.tenDvi, tb.maGoiDvu, dv.tenGoiDvu, tb.ngayDky,tb.ngayHluc, tb.tgian, tb.ngayHetHluc, dmct.tenDmucCtiet) " +
            "FROM ZSSDviThueBao tb " +
            "LEFT JOIN ZSSGoiDvu dv ON tb.maGoiDvu = dv.maGoiDvu " +
            "LEFT JOIN DCDvi dvi ON tb.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet dmct ON tb.tthaiNvu = dmct.maDmuc " +
            "WHERE (:filter IS NULL OR " +
            "LOWER(tb.maDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(tb.zoaId) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvi.tenDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(tb.maGoiDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dv.tenGoiDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(tb.ngayDky) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(tb.ngayHetHluc) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dmct.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<ZSSDviThueBaoResponse> searchAll(@Param("filter") String filter, Pageable pageable);


    boolean existsByMaDvi(String maDvi);
    boolean existsByZoaId(String zoaId);


    @Query(value = "SELECT ZDG.MA_GOI_DVU FROM ZSS_DVI_THUE_BAO zdg \n" +
            "WHERE ZDG.MA_GOI_DVU IN :mgdv", nativeQuery = true)
    List<String> existsByMaGoiDvu(@Param("mgdv") List<String> mgdv);

    List<ZSSDviThueBao> findAllByMaDvi(String maDvi);

    @Query("SELECT distinct tk.maDvi FROM ZssTKhoanZoa tk JOIN ZSSMauZNZ mz ON mz.zoaId = tk.maZoa WHERE mz.zoaId = :zoaId")
    String findMaDviByZoaId(@Param("zoaId") String zoaId);
}
