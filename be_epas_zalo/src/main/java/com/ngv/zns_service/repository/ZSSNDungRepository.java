package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.ZSSNDungResponse;
import com.ngv.zns_service.model.entity.ZSSNDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZSSNDungRepository extends JpaRepository<ZSSNDung, String> {
        @Query("SELECT new com.ngv.zns_service.dto.response.ZSSNDungResponse(" +
                "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.tgianTao, dmct.tenDmucCtiet, nd.tgianGui ) " +
                "FROM ZSSNDung nd " +
                "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
                "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
                "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
                "WHERE (:tuNgay IS NULL OR nd.tgianTao >= :tuNgay) " +
                "AND (:denNgay IS NULL OR nd.tgianTao <= :denNgay) " +
                "AND (:maDvu IS NULL OR nd.maDvu = :maDvu) " +
                "AND (:maDvi IS NULL OR nd.maDvi = :maDvi) " +
                "AND (:tthaiGuizns IS NULL OR nd.tthaiGuizns = :tthaiGuizns)")
        Page<ZSSNDungResponse> search(@Param("tuNgay") String tuNgay,
                                      @Param("denNgay") String denNgay,
                                      @Param("maDvu") String maDvu,
                                      @Param("maDvi") String maDvi,
                                      @Param("tthaiGuizns") String tthaiGuizns,
                                      Pageable pageable);

        @Query("SELECT new com.ngv.zns_service.dto.response.ZSSNDungResponse(" +
                "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.tgianTao, dmct.tenDmucCtiet, nd.tgianGui ) " +
                "FROM ZSSNDung nd " +
                "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
                "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
                "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
                "WHERE (:tuNgay IS NULL OR nd.tgianTao >= :tuNgay) " +
                "AND (:denNgay IS NULL OR nd.tgianTao <= :denNgay) " +
                "AND (:maDvu IS NULL OR nd.maDvu = :maDvu) " +
                "AND (:maDvi IS NULL OR nd.maDvi = :maDvi) " +
                "AND (:tthaiGuizns IS NULL OR nd.tthaiGuizns = :tthaiGuizns)")
        List<ZSSNDungResponse> searchToExcel(@Param("tuNgay") String tuNgay,
                                             @Param("denNgay") String denNgay,
                                             @Param("maDvu") String maDvu,
                                             @Param("maDvi") String maDvi,
                                             @Param("tthaiGuizns") String tthaiGuizns);

        @Query("SELECT new com.ngv.zns_service.dto.response.ZSSNDungResponse(" +
                "nd.maMau, nd.maDvi, dvi.tenDvi, nd.maDvu, dvu.tenDvu, nd.ndung, nd.taiKhoanZalo, nd.tgianTao, dmct.tenDmucCtiet, nd.tgianGui ) " +
                "FROM ZSSNDung nd " +
                "LEFT JOIN DCDvi dvi ON nd.maDvi = dvi.maDvi " +
                "LEFT JOIN ZSSDvu dvu ON nd.maDvu = dvu.maDvu " +
                "LEFT JOIN ZSSDmucCtiet dmct ON nd.tthaiGuizns = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_GUIZNS' " +
                "WHERE (:filter IS NULL OR " +
                "LOWER(nd.maMau) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(nd.maDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(dvi.tenDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(nd.maDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(dvu.tenDvu) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(nd.ndung) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(nd.taiKhoanZalo) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
                "LOWER(dmct.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%'))) ")
        Page<ZSSNDungResponse> searchAll(@Param("filter") String filter, Pageable pageable);

        Optional<ZSSNDung> findByIdMessage(String idMessage);

        int countByMaNdungStartingWith(String prefix);
}
