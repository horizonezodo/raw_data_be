package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.request.MauBangRequest;
import com.ngv.zns_service.model.entity.ZSSMauZNSBang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSMauZNSBangRepository extends JpaRepository<ZSSMauZNSBang, String> {
    List<ZSSMauZNSBang> findAllByMaMau(String maMau);

    void deleteAllByMaMau(String maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.MauBangRequest(t.maMau, t.tdeBang, t.ndungBang) " +
            "FROM ZSSMauZNSBang t WHERE t.maMau IN :maMau")
    List<MauBangRequest> findMauBang(@Param("maMau") List<String> maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.MauBangRequest(t.maMau, t.tdeBang, t.ndungBang) " +
            "FROM ZSSMauZNSBang t WHERE t.maMau = :maMau")
    List<MauBangRequest> findMauBangByMaMau(@Param("maMau") String maMau);
}
