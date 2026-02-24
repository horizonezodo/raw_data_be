package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.request.CtaoGtriTsoRequest;
import com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse;
import com.ngv.zns_service.model.entity.ZSSCtaoGtriTso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSCtaoGtriTsoRepository extends JpaRepository<ZSSCtaoGtriTso, String> {
    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse(" +
            "tso.maCtaoGtriTso, tso.tenCtaoGtriTso, tso.cthucCtaoGtriTso, tso.loaiGiaTri, tso.mdichSdung) " +
            "FROM ZSSCtaoGtriTso tso " +
            "WHERE (:filter IS NULL OR " +
            "LOWER(tso.maCtaoGtriTso) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(tso.tenCtaoGtriTso) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<ZSSCtaoGtriTsoResponse> searchAll(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse(" +
            "tso.maCtaoGtriTso, tso.tenCtaoGtriTso) " +
            "FROM ZSSCtaoGtriTso tso")
    List<ZSSCtaoGtriTsoResponse> searchToExportExcel();

    boolean existsByMaCtaoGtriTso(String maCtaoGtriTso);

    @Query("SELECT COUNT (*) FROM ZSSCtaoGtriTso tso" +
            " WHERE tso.maCtaoGtriTso = :maCtaoGtriTso AND tso.maCtaoGtriTso <> :maCtaoGtriTsoExist")
    Integer checkMaCtaoGtriTsoUpdateExist(@Param("maCtaoGtriTso") String maCtaoGtriTso,
                                          @Param("maCtaoGtriTsoExist") String maCtaoGtriTsoExist) ;

    @Query("SELECT new com.ngv.zns_service.dto.request.CtaoGtriTsoRequest(c.cthucCtaoGtriTso, c.tenCtaoGtriTso, c.loaiGiaTri, c.mdichSdung) " +
            "FROM ZSSCtaoGtriTso c " +
            "WHERE c.maCtaoGtriTso = :maCtaoGtriTso")
    List<CtaoGtriTsoRequest> listByMaCtaoTso(@Param("maCtaoGtriTso") String maCtaoGtriTso);
}
