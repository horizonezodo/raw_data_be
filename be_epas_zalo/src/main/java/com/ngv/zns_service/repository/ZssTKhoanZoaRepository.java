package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZssTKhoanZoaRepository extends JpaRepository<ZssTKhoanZoa, String> {
    ZssTKhoanZoa findZssTKhoanZoaByMaZoa(String oaId);

    List<ZssTKhoanZoa> findAllByMaDvi(String maDvi);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse(" +
            "zoa.maDvi, zoa.maZoa, zoa.tenZoa, dvi.tenDvi, dvi.tenVtat ) " +
            "FROM ZssTKhoanZoa zoa  " +
            "LEFT JOIN DCDvi dvi ON zoa.maDvi = dvi.maDvi " +
            "WHERE (:filter IS NULL OR " +
            "LOWER(zoa.maDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(zoa.maZoa) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(zoa.tenZoa) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvi.tenDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvi.tenVtat) LIKE LOWER(CONCAT('%', :filter, '%'))) ")
    Page<ZssTKhoanZoaResponse> searchAll(@Param("filter") String filter, Pageable pageable);


    @Query("SELECT new com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse(" +
            "zoa.maDvi, zoa.maZoa, zoa.tenZoa, dvi.tenDvi, dvi.tenVtat ) " +
            "FROM ZssTKhoanZoa zoa  " +
            "LEFT JOIN DCDvi dvi ON zoa.maDvi = dvi.maDvi")
    List<ZssTKhoanZoaResponse> searchToExcel();

    @Query("SELECT dvi.maDvi FROM ZssTKhoanZoa tk JOIN DCDvi dvi ON dvi.maDvi = tk.maDvi WHERE tk.maZoa = :zoaId")
    String findMaDviByZoaId(@Param("zoaId") String zoaId);
}
