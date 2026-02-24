package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.ZSSAppResponse;
import com.ngv.zns_service.model.entity.ZssApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZSSAppRepository extends JpaRepository<ZssApp, String> {
    Optional<ZssApp> findByMaDvi(String maDvi);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSAppResponse( " +
            "app.appId, app.maDvi, dv.tenDvi, app.appName, dmct.tenDmucCtiet, null, null, " +
            "null, null, null) " +
            "FROM ZssApp app " +
            "INNER JOIN DCDvi dv ON dv.maDvi = app.maDvi " +
            "INNER JOIN ZSSDmucCtiet dmct ON app.tthaiNvu = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_DVU' " +
            "WHERE (:maDvi IS NULL OR app.maDvi = :maDvi) AND " +
            "(:appId IS NULL OR app.appId = :appId) AND " +
            "(:tenDvi IS NULL OR dv.tenDvi = :tenDvi) AND " +
            "(:filter IS NULL OR " +
            "LOWER(app.appId) lIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(app.appName) lIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(app.maDvi) lIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dv.tenDvi) lIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<ZSSAppResponse> searchAll(@Param("maDvi") String maDvi,
            @Param("appId") String appId,
            @Param("tenDvi") String tenDvi,
            @Param("filter") String filter, Pageable pageable);

    Optional<ZssApp> findByWebhookVerificationFile(String webhookVerificationFile);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSAppResponse( " +
            "app.appId, app.maDvi, dv.tenDvi, app.appName, dmct.tenDmucCtiet, null, null, null, null, null) " +
            "FROM ZssApp app " +
            "INNER JOIN DCDvi dv ON dv.maDvi = app.maDvi " +
            "INNER JOIN ZSSDmucCtiet dmct ON app.tthaiNvu = dmct.maDmucCtiet AND dmct.maDmuc = 'TTHAI_DVU' " +
            "WHERE (:maDvi IS NULL OR app.maDvi = :maDvi) AND " +
            "(:appId IS NULL OR app.appId = :appId) AND " +
            "(:tenDvi IS NULL OR dv.tenDvi = :tenDvi)")
    List<ZSSAppResponse> searchToExcel(@Param("maDvi") String maDvi, @Param("appId") String appId, @Param("tenDvi") String tenDvi);

}
