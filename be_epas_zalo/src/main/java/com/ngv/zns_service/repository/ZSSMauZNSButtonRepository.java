package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.request.MauButtonRequest;
import com.ngv.zns_service.model.entity.ZSSMauZNSButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZSSMauZNSButtonRepository extends JpaRepository<ZSSMauZNSButton, String> {

    List<ZSSMauZNSButton> findAllByMaMau(String maMau);

    void deleteAllByMaMau(String maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.MauButtonRequest(b.maMau, b.loaiButton, b.ndung, b.lket) " +
            "FROM ZSSMauZNSButton b WHERE b.maMau IN :maMau")
    List<MauButtonRequest> findButtonsByMaMaus(@Param("maMau") List<String> maMau);

    @Query("SELECT new com.ngv.zns_service.dto.request.MauButtonRequest(b.maMau, b.loaiButton, b.ndung, b.lket) " +
            "FROM ZSSMauZNSButton b WHERE b.maMau = :maMau")
    List<MauButtonRequest> findButtonsByMaMau(@Param("maMau") String maMau);

    Optional<ZSSMauZNSButton> findByMaMauAndZoaIdAndNdung(String maMau, String zoaId, String ndung);
}
