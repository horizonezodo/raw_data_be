package com.ngv.zns_service.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ngv.zns_service.model.entity.ZSSNDung;
import com.ngv.zns_service.dto.response.ZSSDataMiningResponse;

@Repository
public interface ZSSDataMiningRepository extends JpaRepository<ZSSNDung, String> {
    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDataMiningResponse(" +
            "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.maKhhang, nd.tenKhhang, nd.tgianTao, nd.tgianGui, dmct.tenDmucCtiet, m.donGia ) " +
            "FROM ZSSNDung nd " +
            "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
            "LEFT JOIN ZSSMauZNZ m ON nd.maMau = m.maMau " +
            "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
            "WHERE (:tuNgay IS NULL OR nd.tgianTao >= :tuNgay) " +
            "AND (:denNgay IS NULL OR nd.tgianTao <= :denNgay) " +
            "AND (:maDvi IS NULL OR nd.maDvi = :maDvi) " +
            "AND (:tthaiGuizns IS NULL OR nd.tthaiGuizns = :tthaiGuizns)" +
            "AND (:maDvuList IS NULL OR nd.maDvu IN :maDvuList)" )
    Page<ZSSDataMiningResponse> search(@Param("tuNgay") String tuNgay,
                                       @Param("denNgay") String denNgay,
                                       @Param("maDvi") String maDvi,
                                       @Param("maDvuList") List<String> maDvuList,
                                       @Param("tthaiGuizns") String tthaiGuizns,
                                       Pageable pageable);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDataMiningResponse(" +
            "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.maKhhang, nd.tenKhhang, nd.tgianTao, nd.tgianGui, dmct.tenDmucCtiet, m.donGia ) " +
            "FROM ZSSNDung nd " +
            "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
            "LEFT JOIN ZSSMauZNZ m ON nd.maMau = m.maMau " +
            "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
            "WHERE (:tuNgay IS NULL OR nd.tgianTao >= :tuNgay) " +
            "AND (:denNgay IS NULL OR nd.tgianTao <= :denNgay) " +
            "AND (:maDvi IS NULL OR nd.maDvi = :maDvi) " +
            "AND (:tthaiGuizns IS NULL OR nd.tthaiGuizns = :tthaiGuizns)" +
            "AND (:maDvuList IS NULL OR nd.maDvu IN :maDvuList)" )
    List<ZSSDataMiningResponse> searchToExcel(@Param("tuNgay") String tuNgay,
                                              @Param("denNgay") String denNgay,
                                              @Param("maDvi") String maDvi,
                                              @Param("maDvuList") List<String> maDvuList,
                                              @Param("tthaiGuizns") String tthaiGuizns);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSDataMiningResponse(" +
            "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.maKhhang, nd.tenKhhang, nd.tgianTao, nd.tgianGui, dmct.tenDmucCtiet, m.donGia ) " +
            "FROM ZSSNDung nd " +
            "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
            "LEFT JOIN ZSSMauZNZ m ON nd.maMau = m.maMau " +
            "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
            "WHERE (:filter IS NULL OR " +
            "LOWER(nd.maMau) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.maDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvi.tenDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.maDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvu.tenDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.ndung) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.taiKhoanZalo) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.maKhhang) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(nd.tenKhhang) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dmct.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%'))) ")
    Page<ZSSDataMiningResponse> searchAll(@Param("filter") String filter, Pageable pageable);
}
