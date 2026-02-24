package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.request.CaiDatTSoRequest;
import com.ngv.zns_service.model.entity.ZSSMauZNSTso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZSSMauZNSTsoRepository extends JpaRepository<ZSSMauZNSTso, String> {
    void deleteAllByMaMau(String maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.CaiDatTSoRequest(t.maMau, t.tenTso, t.maCtaoGtriTso, t.paramType, t.ndungTso, t.bbuoc, t.kieuTso, t.soKtuTda, t.soKtuTthieu, t.nhanGtriNull) " +
            "FROM ZSSMauZNSTso t WHERE t.maMau IN :maMau")
    List<CaiDatTSoRequest> findTSoByMaMaus(@Param("maMau") List<String> maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.CaiDatTSoRequest(t.maMau, t.tenTso, t.maCtaoGtriTso, t.paramType, t.ndungTso, t.bbuoc, t.kieuTso, t.soKtuTda, t.soKtuTthieu, t.nhanGtriNull) " +
            "FROM ZSSMauZNSTso t WHERE t.maMau = :maMau")
    List<CaiDatTSoRequest> findTSoByMaMau(@Param("maMau") String maMau);

    List<ZSSMauZNSTso> findByMaMau(String maMau);

    @Query("SELECT c.maCtaoGtriTso FROM ZSSMauZNSTso c WHERE c.maMau = :maMau AND c.zoaId = :zoaId AND c.tenTso = :tenTso")
    List<String> findMaCtaoGtriTsoByMaMauAndZoaIdAndTenTso(
            @Param("maMau") String maMau,
            @Param("zoaId") String zoaId,
            @Param("tenTso") String tenTso
    );

    Optional<ZSSMauZNSTso> findByMaMauAndZoaIdAndTenTso(String maMau, String zoaId, String tenTso);
}
